package com.infocell.giz.gizart.service;

import java.util.List;

import com.infocell.giz.gizart.model.AvailabilityStatus;
import com.infocell.giz.gizart.model.Expert;
import com.infocell.giz.gizart.model.SubSkill;

public interface ExpertService {

	void create(Expert s);

	Expert get(int id);

	Expert getWithSid(String sid);

	List<Expert> getList();

	List<Expert> getWithSkill(SubSkill s);

	List<Expert> getWithSkillAndAvailable(SubSkill s, AvailabilityStatus a);

	void update(Expert s);

	void delete(int id);

}
