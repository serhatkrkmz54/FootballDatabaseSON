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
                transferGecmisi.setTransferType(tg.getTransferType());
                transferGecmisi.setTransferDate(tg.getTransferDate());
                transferGecmisiSet.add(transferGecmisi);
            }
        }
        /*
        {
            "formaNo": 70,
                "pName": "İrfan Can",
                "pSurname": "Eğribayat",
                "pathFile": "",
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
