package db.footballdb.football_d_b_mongo.controller;

import db.footballdb.football_d_b_mongo.domain.Competitions;
import db.footballdb.football_d_b_mongo.model.CountryDTO;
import db.footballdb.football_d_b_mongo.repos.CompetitionsRepository;
import db.footballdb.football_d_b_mongo.service.CountryService;
import db.footballdb.football_d_b_mongo.util.CustomCollectors;
import db.footballdb.football_d_b_mongo.util.WebUtils;
import jakarta.validation.Valid;
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


@Controller
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;
    private final CompetitionsRepository competitionsRepository;

    public CountryController(final CountryService countryService,
            final CompetitionsRepository competitionsRepository) {
        this.countryService = countryService;
        this.competitionsRepository = competitionsRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("toCompetitionsValues", competitionsRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Competitions::getId, Competitions::getCompetitionName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("countries", countryService.findAll());
        return "country/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("country") final CountryDTO countryDTO) {
        return "country/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("country") @Valid final CountryDTO countryDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "country/add";
        }
        countryService.create(countryDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("country.create.success"));
        return "redirect:/countries";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("country", countryService.get(id));
        return "country/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("country") @Valid final CountryDTO countryDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "country/edit";
        }
        countryService.update(id, countryDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("country.update.success"));
        return "redirect:/countries";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = countryService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            countryService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("country.delete.success"));
        }
        return "redirect:/countries";
    }

}
