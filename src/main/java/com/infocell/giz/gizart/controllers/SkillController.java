package com.infocell.giz.gizart.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.infocell.giz.gizart.model.Login;
import com.infocell.giz.gizart.model.Skill;
import com.infocell.giz.gizart.model.SubSkill;
import com.infocell.giz.gizart.service.SkillService;
import com.infocell.giz.gizart.service.SubSkillService;

@Controller
@RequestMapping("/skill")
@SessionAttributes("skillCommand")
public class SkillController {

	@Autowired
	private SkillService skillService;

	@Autowired
	private SubSkillService subSkillService;

	@RequestMapping(value = { "/", "/manage" }, method = RequestMethod.GET)
	public String create(Model model, HttpSession session) {
		Login l = (Login) session.getAttribute("admin");
		try {
			if (l != null && l.getRole().getRoleName().equalsIgnoreCase("admin")) {

				return "createSkill";
			} else {
				return "redirect:/admin/login";

			}
		} catch (NullPointerException e) {
			return "redirect:/admin/login";

		}

	}

	@RequestMapping(value = { "/add" }, method = RequestMethod.POST)
	public String create(@ModelAttribute Skill skill, RedirectAttributes redirectAttribute, SessionStatus status,
			HttpSession session) {

		Login l = (Login) session.getAttribute("admin");
		try {
			if (l != null && l.getRole().getRoleName().equalsIgnoreCase("admin")) {
				skillService.create(skill);
				List<SubSkill> skillSubSkill = subSkillService.getListFromSkill(skill);
				if (skillSubSkill.isEmpty()) {

					System.out.println("inside subskill if");

					Skill s = skillService.getWithSid(skill.getName());
					SubSkill ss = new SubSkill();
					ss.setSkill(s);
					ss.setName(s.getName());
					subSkillService.create(ss);

				}
				redirectAttribute.addFlashAttribute("message", "Added successfully");

				return "redirect:/skill/";

			} else {
				return "redirect:/admin/login";

			}
		} catch (NullPointerException e) {
			return "redirect:/admin/login";

		}

	}

	@RequestMapping(value = "/edit/{sid}")
	public String update(@PathVariable("sid") int sid, Model model, HttpSession session) {

		Login l = (Login) session.getAttribute("admin");
		try {
			if (l != null && l.getRole().getRoleName().equalsIgnoreCase("admin")) {

				Skill s = skillService.get(sid);
				List<SubSkill> subSkillList = subSkillService.getListFromSkill(s);
				model.addAttribute("subSkillSet", subSkillList);
				model.addAttribute("editCommand", skillService.get(sid));
				model.addAttribute("subSkillCommand", new SubSkill());
				return "editSkill";

			} else {
				return "redirect:/admin/login";

			}
		} catch (NullPointerException e) {
			return "redirect:/admin/login";

		}

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String update(@ModelAttribute("editCommand") Skill skill, HttpSession session, Model model,
			RedirectAttributes rd) {

		Login l = (Login) session.getAttribute("admin");
		try {
			if (l != null && l.getRole().getRoleName().equalsIgnoreCase("admin")) {

				model.addAttribute("skillSet", skillService.getList());
				System.out.println("skill id is " + skill.getSkillId());

				skillService.update(skill);

				rd.addFlashAttribute("message", "Successfully Updated");
				return "redirect:/skill/manage";

			} else {
				return "redirect:/admin/login";

			}
		} catch (NullPointerException e) {
			return "redirect:/admin/login";

		}

	}

	@RequestMapping(value = "/addsubskill/{skillId}")
	public String addSubSkill(@PathVariable("skillId") int skillId, HttpSession session, Model model) {

		Login l = (Login) session.getAttribute("admin");
		try {
			if (l != null && l.getRole().getRoleName().equalsIgnoreCase("admin")) {

				model.addAttribute("addSubSkillCommand", new SubSkill());

				model.addAttribute("subSkillList", subSkillService.getListFromSkill(skillId));
				model.addAttribute("skillId", skillId);

				return "createSubSkill";

			} else {
				return "redirect:/admin/login";

			}
		} catch (NullPointerException e) {
			return "redirect:/admin/login";

		}

	}

	@RequestMapping(value = "/addsubskill", method = RequestMethod.POST)
	public String postSubSkill(@RequestParam("skillId") int skillId, HttpSession session, Model model,
			@ModelAttribute("addSubSkillCommand") SubSkill subSkill, RedirectAttributes rd) {

		Login l = (Login) session.getAttribute("admin");
		try {
			if (l != null && l.getRole().getRoleName().equalsIgnoreCase("admin")) {

				Skill s = skillService.get(skillId);
				subSkill.setSkill(s);

				subSkillService.create(subSkill);

				rd.addFlashAttribute("message", "Successfully added a subskill to " + s.getName());
				return "redirect:/skill/manage";

			} else {
				return "redirect:/admin/login";

			}
		} catch (NullPointerException e) {
			return "redirect:/admin/login";

		}

	}

	@RequestMapping(value = "/edit-subskill/{skillId}")
	public String editSubSkill(@PathVariable("skillId") int skillId, HttpSession session, Model model) {

		Login l = (Login) session.getAttribute("admin");
		try {
			if (l != null && l.getRole().getRoleName().equalsIgnoreCase("admin")) {

				model.addAttribute("subSkill", subSkillService.get(skillId));

				return "editSubSkill";

			} else {
				return "redirect:/admin/login";

			}
		} catch (NullPointerException e) {
			return "redirect:/admin/login";

		}

	}

	@RequestMapping(value = "/edit-subskill", method = RequestMethod.POST)
	public String editSubSkill(@ModelAttribute("editSubSkillCommand") SubSkill subSkill, HttpSession session,
			RedirectAttributes rd) {

		Login l = (Login) session.getAttribute("admin");
		try {
			if (l != null && l.getRole().getRoleName().equalsIgnoreCase("admin")) {

				subSkillService.update(subSkill);
				rd.addFlashAttribute("message", "Successfully Edited");
				return "redirect:/skill/manage";

			} else {
				return "redirect:/admin/login";

			}
		} catch (NullPointerException e) {
			return "redirect:/admin/login";

		}

	}

	@RequestMapping(value = "/remove/{skillId}")
	public String delete(@PathVariable("skillId") int skillId, HttpSession session, RedirectAttributes rd) {
		Login l = (Login) session.getAttribute("admin");
		try {
			if (l != null && l.getRole().getRoleName().equalsIgnoreCase("admin")) {

				skillService.delete(skillId);
				rd.addFlashAttribute("message", "Successfully Deleted");
				return "redirect:/skill/manage";

			} else {
				return "redirect:/admin/login";

			}
		} catch (NullPointerException e) {
			return "redirect:/admin/login";

		}

	}

	@ModelAttribute("skillcommand")
	public Skill skillCommand() {
		return new Skill();
	}

	@ModelAttribute("skillList")
	public List<Skill> getSkillList() {
		return skillService.getList();
	}

}
