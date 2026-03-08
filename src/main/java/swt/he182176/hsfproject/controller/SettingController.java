package swt.he182176.hsfproject.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swt.he182176.hsfproject.dto.SettingDTO;
import swt.he182176.hsfproject.entity.Setting;
import swt.he182176.hsfproject.service.SettingService;

import java.util.List;

@Controller
@RequestMapping("/settings")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @GetMapping
    public String showList(
            @RequestParam(required = false) Integer typeId,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model
    ) {
        List<Setting> settingList = settingService.search(typeId, status, keyword, sortField, sortDir);

        model.addAttribute("settingList", settingList);
        model.addAttribute("types", settingService.getAllTypes());

        model.addAttribute("typeId", typeId);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "setting-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("settingDTO", new SettingDTO());
        model.addAttribute("types", settingService.getAllTypes());
        return "setting-detail";
    }

    @GetMapping("/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("settingDTO", settingService.getDTOById(id));
        model.addAttribute("types", settingService.getAllTypes());
        return "setting-detail";
    }

    @PostMapping("/save")
    public String saveSetting(
            @Valid @ModelAttribute("settingDTO") SettingDTO settingDTO,
            BindingResult result,
            Model model,
            RedirectAttributes ra
    ) {
        if (result.hasErrors()) {
            model.addAttribute("types", settingService.getAllTypes());
            return "setting-detail";
        }

        try {
            settingService.save(settingDTO);
            ra.addFlashAttribute("msg", "Save setting successfully");
            return "redirect:/settings";
        } catch (Exception e) {
            model.addAttribute("err", e.getMessage());
            model.addAttribute("types", settingService.getAllTypes());
            return "setting-detail";
        }
    }

    @GetMapping("/{id}/toggle")
    public String toggleStatus(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            settingService.toggleStatus(id);
            ra.addFlashAttribute("msg", "Change setting status successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("err", e.getMessage());
        }
        return "redirect:/settings";
    }
}