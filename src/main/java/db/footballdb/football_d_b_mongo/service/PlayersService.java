package db.footballdb.football_d_b_mongo.service;

import db.footballdb.football_d_b_mongo.domain.Country;
import db.footballdb.football_d_b_mongo.domain.Players;
import db.footballdb.football_d_b_mongo.domain.Teams;
import db.footballdb.football_d_b_mongo.domain.TransferGecmisi;
import db.footballdb.football_d_b_mongo.model.PlayersDTO;
import db.footballdb.football_d_b_mongo.model.TeamsDTO;
import db.footballdb.football_d_b_mongo.repos.CountryRepository;
import db.footballdb.football_d_b_mongo.repos.PlayersRepository;
import db.footballdb.football_d_b_mongo.repos.TeamsRepository;
import db.footballdb.football_d_b_mongo.util.NotFoundException;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.micrometer.common.util.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


@Service
public class PlayersService {

    private final PlayersRepository playersRepository;
    private final TeamsRepository teamsRepository;
    private final CountryRepository countryRepository;

    public PlayersService(final PlayersRepository playersRepository,
            final TeamsRepository teamsRepository, final CountryRepository countryRepository) {
        this.playersRepository = playersRepository;
        this.teamsRepository = teamsRepository;
        this.countryRepository = countryRepository;
    }

    public List<PlayersDTO> findAll() {
        final List<Players> playerses = playersRepository.findAll(Sort.by("id"));
        return playerses.stream()
                .map(players -> mapToDTO(players, new PlayersDTO()))
                .toList();
    }
    public BigDecimal getOyuncuDegeri() {
        List<Players> oyuncuListesi = playersRepository.findAll();
        BigDecimal oyuncuToplamDeger = oyuncuListesi.stream()
                .map(Players::getPValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return oyuncuToplamDeger;
    }

    public Page<PlayersDTO> findPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber-1,10);
        return playersRepository.findAll(pageable).map(players -> mapToDTO(players, new PlayersDTO()));
    }

    public PlayersDTO get(final Long id) {
        return playersRepository.findById(id)
                .map(players -> mapToDTO(players, new PlayersDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PlayersDTO playersDTO) throws IOException{
        final Players players = new Players();
        mapToEntity(playersDTO, players);
        return playersRepository.save(players).getId();
    }

    public void update(final Long id, final PlayersDTO playersDTO) {
        final Players players = playersRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        try {
            mapToEntity(playersDTO, players);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        playersRepository.save(players);
    }

    public void delete(final Long id) {
        playersRepository.deleteById(id);
    }

    public PlayersDTO mapToDTO(final Players players, final PlayersDTO playersDTO) {
        playersDTO.setId(players.getId());
        playersDTO.setPName(players.getPName());
        playersDTO.setPSurname(players.getPSurname());
        playersDTO.setFormaNo(players.getFormaNo());
        playersDTO.setPCountry(players.getPCountry());
        playersDTO.setPWeight(players.getPWeight());
        playersDTO.setPHeight(players.getPHeight());
        playersDTO.setFilePath((players.getPathFile() == null || players.getPathFile().isEmpty()) ? null : players.getPathFile());
        playersDTO.setPPosition(players.getPPosition());
        playersDTO.setPPlayerAge(players.getPPlayerAge());
        playersDTO.setPValue(players.getPValue());
        playersDTO.setPFoot(players.getPFoot());
        playersDTO.setToTeams(players.getToTeams() == null ? null : players.getToTeams().getId());
        playersDTO.setToCountryPlayers(players.getToCountryPlayers() == null ? null : players.getToCountryPlayers().getId());
        playersDTO.setOyuncuHangiTakimda(players.getToTeams() == null ? null : players.getToTeams().getTName());
        playersDTO.setOyuncuHangiUlkede(players.getToCountryPlayers() == null ? null : players.getToCountryPlayers().getCName());
        playersDTO.setTransferGecmisi(players.getTransferGecmisi());
        return playersDTO;
    }

    private Players mapToEntity(final PlayersDTO playersDTO, final Players players) throws IOException {
        ArrayList<TransferGecmisi> transferGecmisiSet = new ArrayList<>();
        players.setId(players.getId());
        players.setPName(playersDTO.getPName());
        players.setPSurname(playersDTO.getPSurname());
        players.setFormaNo(playersDTO.getFormaNo());
        players.setPCountry(playersDTO.getPCountry());
        players.setPWeight(playersDTO.getPWeight());
        players.setPHeight(playersDTO.getPHeight());
        players.setPPosition(playersDTO.getPPosition());
        players.setPPlayerAge(playersDTO.getPPlayerAge());
        players.setPValue(playersDTO.getPValue());
        players.setPFoot(playersDTO.getPFoot());
        if (playersDTO.getPathFile()  != null && !playersDTO.getPathFile().isEmpty()) {
            byte[] image = Base64.encodeBase64(playersDTO.getPathFile().getBytes(), false);
            String result = new String(image);
            players.setPathFile(result);
        } else {
            if (playersDTO.getFilePath() == null || playersDTO.getFilePath().isEmpty()){
                String base64Image = "src/main/resources/static/images/default-players-pic.png";
                byte[] imageBytes = Base64.encodeBase64(Files.readAllBytes(Paths.get(base64Image)), true);
                String defaultResult = new String(imageBytes);
                players.setPathFile(defaultResult);
            } else {
                players.setPathFile(playersDTO.getFilePath());
            }
        }

        if (players.getTransferGecmisi() != null && !players.getTransferGecmisi().isEmpty()) {
            transferGecmisiSet.addAll(players.getTransferGecmisi());
            for (TransferGecmisi tg : playersDTO.getTransferGecmisi()) {
                TransferGecmisi transferGecmisi = new TransferGecmisi();
                transferGecmisi.setToTeam(tg.getToTeam());
                transferGecmisi.setFromTeam(tg.getFromTeam());
                transferGecmisi.setTransferFee(tg.getTransferFee());
                transferGecmisiSet.add(transferGecmisi);
            }
        }

        /*
        {
            "formaNo": 70,
                "pName": "İrfan Can",
                "pSurname": "Eğribayat",
                "pathFile": "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",
                "pCountry": "Türkiye",
                "pWeight": 85,
                "pHeight": 193,
                "pPosition": "Kaleci",
                "pPlayerAge": "25",
                "pValue": {
            "$numberDecimal": "1800000"
        },
            "pFoot": "Sağ Ayak",
                "toTeams": {
            "$numberLong": "10044"
        },
            "toCountryPlayers": {
            "$numberLong": "10002"
        },
            "dateCreated": {
            "dateTime": {
                "$date": "2024-03-18T11:33:10.713Z"
            },
            "offset": "+03:00"
        },
            "lastUpdated": {
            "dateTime": {
                "$date": "2024-03-18T11:33:10.713Z"
            },
            "offset": "+03:00"
        },
            "version": 0,
                "_class": "db.footballdb.football_d_b_mongo.domain.Players",
                "transferGecmisi": [
            {
                "toTeam": {
                "$numberLong": "10044"
            },
                "fromTeam": {
                "$numberLong": "10044"
            },
                "transferFee": {
                "$numberDecimal": "1244554"
            }
            }
  ]
        }
         */

        players.setTransferGecmisi(transferGecmisiSet);
        final Teams toTeams = playersDTO.getToTeams() == null ? null : teamsRepository.findById(playersDTO.getToTeams())
                .orElseThrow(() -> new NotFoundException("toTeams not found"));
        players.setToTeams(toTeams);
        final Country toCountryPlayers = playersDTO.getToCountryPlayers() == null ? null : countryRepository.findById(playersDTO.getToCountryPlayers())
                .orElseThrow(() -> new NotFoundException("toCountryPlayers not found"));
        players.setToCountryPlayers(toCountryPlayers);
        return players;
    }

}
