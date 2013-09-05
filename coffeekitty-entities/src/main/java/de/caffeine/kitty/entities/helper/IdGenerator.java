package de.caffeine.kitty.entities.helper;

import java.util.UUID;

public class IdGenerator {
	
    private IdGenerator() {
		super();
	}

	public static String createId() {
        UUID uuid = java.util.UUID.randomUUID();
        return uuid.toString();
    }
}