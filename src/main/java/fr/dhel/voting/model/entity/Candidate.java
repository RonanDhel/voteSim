package fr.dhel.voting.model.entity;

import java.util.Objects;

import lombok.Getter;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import fr.dhel.voting.model.entity.politicalpos.PoliticalPosition;

/**
 * Represente un Candidat pour l'élection avec un nom, un positionnement sur
 * l'échiquier politique et un niveau de maîtrise du langage politique.
 * 
 * @author Ronan
 *
 */
@ToString
public class Candidate {
	@Getter
	private final int id;
	
	private final String name;

	@Getter
	private final PoliticalPosition politicalPosition;

	@Getter
	private final PoliticalSkill politicalSkill;

	public Candidate(
			final int id, final String name, final PoliticalPosition politicalPosition,
			final PoliticalSkill politicalSkill) {
		if (StringUtils.isBlank(name))
			throw new IllegalArgumentException("name should not be empty");
		
		this.id = id;
		this.name = name;
		this.politicalPosition = Objects.requireNonNull(politicalPosition,
				"politicalPosition should not be null");
		this.politicalSkill = Objects.requireNonNull(politicalSkill,
				"politicalSkill should not be null");
	}

	//===================================================================
	// METHODES
	//===================================================================
	
	public String name() {
		return name;
	}
}
