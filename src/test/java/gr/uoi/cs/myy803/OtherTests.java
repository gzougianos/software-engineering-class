package gr.uoi.cs.myy803;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import myy803.model.Command;

public class OtherTests {

	@Test
	public void allCommandsAreDeclared() {
		for (Command c : Command.values()) {
			assertNotNull(c.getContent());
		}
	}

}
