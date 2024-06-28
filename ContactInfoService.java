package ContactService.services;
/*
 * Author: John Miller
 * CS - 320
 * Milestone1
 * ContactInfoService class contains required functionality to add, delete, find, or update contactInfo.
 * Built with help from instructor video tutorial (Clinton Bush).
 */



import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import Validation.EntityValidator;
import _ContactService.entities.ContactInfo;


public class ContactInfoService {
	
	public static ContactInfoService INSTANCE;
	
	final Map<String, ContactInfo> entityRepository;
	final EntityValidator validator;
	
	// Singleton Constructor
	private ContactInfoService() {
		entityRepository = new ConcurrentHashMap<>();
		this.validator = new EntityValidator();
		}
	
	// Checks to see if there is already an instance of ContactInfoService returns new one if none found.
	public static synchronized ContactInfoService getInstance() {
		if(INSTANCE == null)
		{
			INSTANCE = new ContactInfoService();
		}
		return INSTANCE;
	}
	
	// Creates new object of ContactInfo using EntityValidator to ensure constraints
	public ContactInfo create(final ContactInfo contactInfo) {
		Objects.requireNonNull(contactInfo);
		
		return validator.validateAndDoOrThrow(
				contactInfo, 
				c -> {
		
					if(entityRepository.containsKey(contactInfo.contactID())) {
						throw new IllegalArgumentException(String.format("An entry already exist with ID [%s]. Did you mean to update?", contactInfo.contactID()));
					}
					return entityRepository.put(contactInfo.contactID(), contactInfo);
				}
			);
	}
	
	// Iterates over HashMap for matching ID returns optional NULL if none found. Removes match if found.
	public Optional<ContactInfo> deleteById(final String id){
		Objects.requireNonNull(id); // Optional null pointer
		return Optional.ofNullable(entityRepository.remove(id)); // Returns either null pointer or removes matching id from map.
	}
	
	// Iterates over HashMap for matching ID returns a ContactInfo Object if found, NULL if not.
	public ContactInfo findById(final String id) {
		Objects.requireNonNull(id); // Optional null pointer
		if (!entityRepository.containsKey(id)) {
			throw new IllegalArgumentException(String.format("No Entry with ID %s$] exists. Did you mean to create?", id));
		}
		return entityRepository.get(id);
	}
	
	// Iterates over HashMap for matching ID updates mutable field if found. Returns NULL if not.
	public ContactInfo update(final ContactInfo contactInfo){
		Objects.requireNonNull(contactInfo); 
		
		// Calls EntityValidator to ensure contactInfo has correct field constraints.
		return validator.validateAndDoOrThrow(
				contactInfo, 
				c -> {
					if(!entityRepository.containsKey(contactInfo.contactID())) { // If no matching ID exists throws error
						throw new IllegalArgumentException(String.format("No entry with ID [%s] exist. Did you mean to create?", contactInfo.contactID()));
					}
					return entityRepository.put(contactInfo.contactID(), contactInfo); // Return updated contactInfo to c if no error found.
				}
			);

	}

	
}
