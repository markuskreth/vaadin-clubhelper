package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

import org.slf4j.Logger;

/**
 * Writes to a file catching all IOExceptions.
 * @author markus
 *
 */
public class FileExporter implements AutoCloseable {

	private final Logger log;
	private final BufferedWriter out;

	public void writeLine(String prettyString) {
		if (out != null) {
			try {
				out.write(prettyString);
				out.newLine();
			} catch (IOException e) {
				log.error("Error on write operation.", e);
			}
		}
	}
	
	public static Builder builder(Logger log) {
		Builder b = new Builder();
		b.setLog(log);
		return b;
	}
	

	public static class Builder {
		
		private Logger log;
		private String fileName;
		private boolean append = true;
		private boolean enabled = true;
		
		private Builder() {
		}
		
		public Builder setLog(Logger log) {
			this.log = log;
			return this;
		}
		
		public Builder setFileName(String fileName) {
			this.fileName = fileName;
			return this;
		}
		
		public Builder setAppend(boolean append) {
			this.append = append;
			return this;
		}

		public Builder setEnabled(boolean enabled) {
			this.enabled = enabled;
			return this;
		}

		public Builder disable() {
			this.enabled = false;
			return this;
		}
		
		public FileExporter build() {
			if (!enabled) {
				return new FileExporter(log, null);
			}
			final File f = new File(fileName);
			if (append == false) {

				if (f.exists()) {
					try {
						Files.delete(f.toPath());
					} catch (IOException e) {
						log.error("Error deleting file " + f.getAbsolutePath(), e);
					}
				}
			}
			try {
				return new FileExporter(this);
			} catch (IOException e) {
				log.error("Error creating Writer " + f.getAbsolutePath(), e);
				log.warn("No Output written.");
				return new FileExporter(log, null);
			}
		}
	}

	private FileExporter(Builder builder) throws IOException {
		this(builder.log, new FileWriter(builder.fileName));
	}

	FileExporter(Logger log, Writer writer) {
		this.log = log;
		if (writer == null) {
			this.out = null;
		} else {
			this.out = new BufferedWriter(writer);
		}
	}
	@Override
	public void close() throws Exception {
		if (out != null) {
			out.close();
		}
	}

}
