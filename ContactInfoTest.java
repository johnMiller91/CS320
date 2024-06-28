/*
 * Author: John Miller
 * CS-320 Milestone 1
 * Contains testCases for ContactInfo records.
 * Makes use of Parameterized tests that feed arguments with invalid fields.
 */

package _ContactService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ContactService.services.ContactInfoService;
import _ContactService.entities.ContactInfo;
import jakarta.validation.ValidationException;

public class ContactInfoTest {
	
	ContactInfoService service;

	@BeforeEach
	void init() {
		ContactInfoService.INSTANCE = null;
		service = ContactInfoService.getInstance();
	}
	
	// Provides Stream of invalid contacts.
	static Stream<Arguments> contactInfoProvider () {
		
		return Stream.of(
				Arguments.of("1", null, "Miller", "1234567890", "123 Fake St.", "firstName is a required field"), // null firstName
				Arguments.of("01234567890", "John", "Miller", "1234567890", "123 Fake St.", "contactID cannot be longer than 10 characters"), // id is too long
				Arguments.of(null, "John", "Miller", "1234567890", "123 Fake St.", "contactID is a required field" ), // null id
				Arguments.of("1", "ABCEDEFJKLM", "Miller", "1234567890", "123 Fake St.", "firstName cannot be longer than 10 characters"), // firstName too long
				Arguments.of("1", "John", "Miller", null, "123 Fake St.", "Phone is a required field"), // null contactPhone
				Arguments.of("1", "John", "Miller", "12345", "123 Fake st.", "Phone must be exactly 10 digits"), // contactPhone too short
				Arguments.of("1", "John", "Miller", "01234567890", "123 Fake st.", "Phone must be exactly 10 digits"), // contactPhone is too long
				Arguments.of("1", "John", "Miller", "ABCDEFHIJK", "123 Fake st.", "Phone must be exactly 10 digits"), // contactPhone has no digits
				Arguments.of("1", "John", null, "0123456789", "123 Fake St.", "lastName is a required field"), // lastName is null
				Arguments.of("1", "John", "Miller1234567890", "1234567890", "123 Fake St.", "lastName cannot be longer than 10 characters"), // lastName is too long
				Arguments.of("1", "John", "Miller", "1234567890", null, "contactAddress is a required field"), // contactAddress is null
				Arguments.of("1", "John", "Miller", "1234567890", 
						"123 Fake St., Fakington, South Carolina, United States of America", "contactAddress cannot be longer than 30 characters")); // contactAddress is too long 
	}
	
	// Test Case for invalid Tasks.
	@ParameterizedTest
	@MethodSource("contactInfoProvider")
	public void testContacts(String id, String firstName, String lastName, String contactPhone, String contactAddress, String expectedMessage) {
		final ContactInfo testCase = new ContactInfo(id, firstName, lastName, contactPhone, contactAddress);
		
		ValidationException exception = assertThrows(ValidationException.class, () ->
			service.create(testCase));
		
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	
	}
	
	// Test case for valid contact
	@Test
	public void validContact() {
		final ContactInfo validContact = new ContactInfo("1", "John", "Miller", "1234567890", "123 Fake st.");
		
		service.create(validContact);
		
		assertEquals(service.findById("1"), validContact);
	}	
}
