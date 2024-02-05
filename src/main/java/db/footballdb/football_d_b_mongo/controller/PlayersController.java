package db.footballdb.football_d_b_mongo.controller;

import db.footballdb.football_d_b_mongo.domain.Country;
import db.footballdb.football_d_b_mongo.domain.Teams;
import db.footballdb.football_d_b_mongo.model.PlayersDTO;
import db.footballdb.football_d_b_mongo.model.TeamsDTO;
import db.footballdb.football_d_b_mongo.repos.CountryRepository;
import db.footballdb.football_d_b_mongo.repos.TeamsRepository;
import db.footballdb.football_d_b_mongo.service.PlayersService;
import db.footballdb.football_d_b_mongo.util.CustomCollectors;
import db.footballdb.football_d_b_mongo.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


@Controller
@RequestMapping("/players")
public class PlayersController {

    private final PlayersService playersService;
    private final TeamsRepository teamsRepository;
    private final CountryRepository countryRepository;

    public PlayersController(final PlayersService playersService,
            final TeamsRepository teamsRepository, final CountryRepository countryRepository) {
        this.playersService = playersService;
        this.teamsRepository = teamsRepository;
        this.countryRepository = countryRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("toTeamsValues", teamsRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Teams::getId, Teams::getTName)));
        model.addAttribute("toCountryPlayersValues", countryRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Country::getId, Country::getCName)));
    }

    @GetMapping
    public String list(final Model model) {
        return getOnePage(model, 1);
    }

    @GetMapping("/list/page/{pageNumber}")
    public String getOnePage(final Model model,@PathVariable("pageNumber") int currentPage) {
        Page<PlayersDTO> page = playersService.findPage(currentPage);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<PlayersDTO> playerses = page.getContent();
        BigDecimal oyuncuToplamDeger = playersService.getOyuncuDegeri();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("playerses", playerses);
        model.addAttribute("oyuncuToplamDeger", oyuncuToplamDeger);
        return "players/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("players") final PlayersDTO playersDTO) {
        return "players/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("players") @Valid final PlayersDTO playersDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            return "players/add";
        }
        playersService.create(playersDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("players.create.success"));
        return "redirect:/players";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("players", playersService.get(id));
        return "players/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("players") @Valid final PlayersDTO playersDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            return "players/edit";
        }
        playersService.update(id, playersDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("players.update.success"));
        return "redirect:/players";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        playersService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("players.delete.success"));
        return "redirect:/players";
    }

}
