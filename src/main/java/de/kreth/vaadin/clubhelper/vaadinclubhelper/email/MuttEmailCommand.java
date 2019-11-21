package de.kreth.vaadin.clubhelper.vaadinclubhelper.email;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MuttEmailCommand implements EmailCommand {

	@Override
	public void send(Email email) throws IOException {

		final List<String> arguments = createArguments(email);
		final List<String> errors = new ArrayList<>();

		File log = new File("email.log");
		List<String> adresses = email.getEmails();
		for (String adress : adresses) {
			List<ProcessBuilder> builders = new ArrayList<>();

			ProcessBuilder echoProcess = new ProcessBuilder("echo", quote(email.getMessage()));
			echoProcess.redirectOutput(Redirect.PIPE);
			builders.add(echoProcess);

			List<String> command = new ArrayList<>(arguments);
			command.add(adress);
			ProcessBuilder procBuilder = new ProcessBuilder(command);
			procBuilder.redirectOutput(log);
			builders.add(procBuilder);

			List<Process> pipeline = ProcessBuilder.startPipeline(builders);
			Process last = pipeline.get(pipeline.size() - 1);
			try {

//				new BufferedReader(new InputStreamReader(last.getInputStream())).lines().collect(Collectors.counting());

				int result = last.waitFor();
				if (result != 0) {
					errors.add(adress);
				}
			}
			catch (InterruptedException e) {
			}

		}

		if (errors.isEmpty() == false) {
			throw new RuntimeException("Errors: " + errors);
		}
	}

	private String quote(String text) {
		return "\"" + text + "\"";
	}

	private List<String> createArguments(Email email) {
		List<String> arguments = new ArrayList<>();
		arguments.add("mutt");
		arguments.add("-s");
		arguments.add(quote(email.getSubject()));
//
//		List<Path> attachements = email.getAttachements();
//		for (Path path : attachements) {
//			arguments.add("-a");
//			arguments.add(quote(path.toAbsolutePath().toString()));
//		}

		arguments = Collections.unmodifiableList(arguments);
		return arguments;
	}

}
