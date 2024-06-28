/*
 * Author: John Miller
 * CS - 320
 * Milestone1
 * ContactInfo record utilizes annotation constraints to ensure each field is not null and within required length.
 * Built with instructor video tutorial (Clinton Bush).
 */


package _ContactService.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContactInfo(
		@NotBlank(message = "contactID is a required field")
		@Size(max = 10, message = "contactID cannot be longer than {max} characters")
		String contactID,
		
		@NotBlank(message = "firstName is a required field")
		@Size(max = 10, message = "firstName cannot be longer than {max} characters")
		String firstName,
		
		@NotBlank(message = "lastName is a required field")
		@Size(max = 10, message = "lastName cannot be longer than {max} characters")
		String lastName,
		
		@NotBlank(message = "Phone is a required field")
		@Pattern(regexp = "\\d{10}", message = "Phone must be exactly 10 digits")
		String contactPhone,
		
		@NotBlank(message = "contactAddress is a required field")
		@Size(max = 30, message = "contactAddress cannot be longer than {max} characters")
		String contactAddress
		) 
{
	

}
