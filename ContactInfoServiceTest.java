package _ContactService;

/*
 * Author: John Miller
 * CS - 320
 * Milestone1
 * ContactInfoServiceTest contains test units for the ContactInfoService class.
 * Built with help from instructor tutorial video (Clinton Bush). 
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ContactService.services.ContactInfoService;
import _ContactService.entities.ContactInfo;

public class ContactInfoServiceTest {
	
	ContactInfoService service;
	
	// Throws out each service instance and assigns a new one before each test.
	@BeforeEach
	void init() {
		ContactInfoService.INSTANCE = null;
		service = ContactInfoService.getInstance();
	}
	
	@Test
	public void create_PreventsAddingExistingContact() {
		final String id = "1234567890";
		final ContactInfo c = new ContactInfo(id, "John", "Miller", "0123456789", "123 Fake St.");
		
		service.create(c);
		
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.create(new ContactInfo(id,"John", "Miller", "0123456789", "123 Fake St.")));
		
		final String expectedMessage = String.format("An entry already exist with ID [%s]. Did you mean to update?", c.contactID());
		final String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void deleteById_RemovesContact(){
		final String id = "1234567890";
		final ContactInfo c = new ContactInfo(id, "John", "Miller", "0123456789", "123 Fake St.");
		
		service.create(c);
		
		Optional<ContactInfo> deleted = service.deleteById(id);
		
		assertTrue(deleted.isPresent()); // Asserts that an object with deletedID exist
		assertEquals(c, deleted.get());  // Asserts that c and the deleted object contain the same data.
		assertThrows(IllegalArgumentException.class, () -> service.findById(id)); // Asserts that the id assigned is no longer present in the map.
		
	}
	
	@Test
	public void findById_ReturnsContactInfoIfIdIsFound() {
		final String id = "1234567890";
		final ContactInfo c = new ContactInfo(id, "John", "Miller", "0123456789", "123 Fake St.");
		
		service.create(c);
		
		// Creates a contactInfo object found using the findById function.
		final ContactInfo found = service.findById(id);
		
		assertEquals(c, found); // Asserts that the found object and c contain the same data.
	}
	

	@Test
	public void update_UpdatesMutableFieldsOfContactInfo(){
		final String id = "1234567890";
		final ContactInfo c = new ContactInfo(id, "John", "Miller", "0123456789", "123 Fake St.");
		
		service.create(c);
		
		// Creates a new object to replace c with same id.
		final ContactInfo updatedC = new ContactInfo(id, "Casey", "Butler", "1234567890", "124 Fake St.");
		
		// Uses the update function to replace c with the updated object. 
		service.update(updatedC);
		
		assertEquals(updatedC, service.findById(id)); // Asserts updatedC has the same data as object now in service. 
		assertEquals(updatedC.firstName(), service.findById(id).firstName()); // Asserts that updatedC has the same first name as the updated object.
		assertEquals(updatedC.lastName(), service.findById(id).lastName()); // Asserts that updatedC has the same last name as the updated object.
		assertEquals(updatedC.contactPhone(), service.findById(id).contactPhone()); // Asserts that updatedC has the same phone as the updated object
		assertEquals(updatedC.contactAddress(), service.findById(id).contactAddress()); // Asserts that updatedC has the same address as the updated object.
		
	}
	
	@Test
	public void update_PreventsUpdateWithoutMatchingID() {
		final String id = "1234567890";
		final ContactInfo c = new ContactInfo(id, "John", "Miller", "1234567890", "123 Fake St");
		
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.update(c));
		
		String expectedMessage = String.format("No entry with ID [%s] exist. Did you mean to create?", id);
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}
}
