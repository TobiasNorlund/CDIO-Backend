package edu.wildlifesecurity.backend.repository.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import edu.wildlifesecurity.backend.MapEntryConverter;
import edu.wildlifesecurity.framework.AbstractComponent;
import edu.wildlifesecurity.framework.EventDispatcher;
import edu.wildlifesecurity.framework.EventType;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.ISubscription;
import edu.wildlifesecurity.framework.LogEvent;
import edu.wildlifesecurity.framework.repository.IRepository;
import edu.wildlifesecurity.framework.tracking.Capture;

/**
 * Implements a repository which uses files to store configuration and logging
 * 
 * @author Tobias
 *
 */
public class FileRepository extends AbstractComponent implements IRepository {

	private Map<String, Object> configuration;
	private EventDispatcher<LogEvent> logEventDispatcher = new EventDispatcher<LogEvent>();
	private String imagePathFormat="Captures/%d.png";
	
	//for more advanced formatting
	private DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat time = new SimpleDateFormat("HH:mm:ss");


	private String getPath(int id) {
		// Create path if it does not exist
		new File(String.format(imagePathFormat, id)).getParentFile().mkdirs();
		
		return String.format(imagePathFormat, id);
	}
	
	
	@Override
	public void init() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadConfiguration(Map<String, Object> configuration) {
		try {
			XStream magicApi = new XStream();
			magicApi.registerConverter(new MapEntryConverter());
			magicApi.alias("configuration", Map.class);

			this.configuration = configuration;
			this.configuration.putAll((Map<String, Object>) magicApi
					.fromXML(new String(Files.readAllBytes(Paths
							.get("configuration.xml")))));
			configuration = this.configuration;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			error("Error in FileRepository. Could not load configuration file:\n"
					+ ioe.getMessage());
		}
	}

	/**
	 * Stores a capture
	 * 
	 * @param capture
	 */
	@Override
	public void storeCapture(Capture capture) {
		try {
			List<Capture> captures = getCaptureDefinitions();

			FileWriter fw = new FileWriter("captures.xml", false);
			XStream magicApi = new XStream();
			magicApi.alias("Capture", Capture.class);
			magicApi.omitField(Capture.class, "regionImage");

			captures.add(capture);

			fw.write(magicApi.toXML(captures));
			fw.close();

			if (capture.regionImage != null) {
				Imgproc.cvtColor(capture.regionImage, capture.regionImage, Imgproc.COLOR_RGB2BGR); 
				Highgui.imwrite(getPath(capture.id), capture.regionImage); // this is wrong now
			}
		} catch (IOException e) {
			e.printStackTrace();
			error("Error in FileRepository. Could not store a new capture: "
					+ e.getMessage());
		}

	}

	/**
	 * Fetches all definitions
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Capture> getCaptureDefinitions() {
		String fileName="captures.xml";
		try {
			FileReader reader = new FileReader(fileName);
			XStream magicApi = new XStream();
			magicApi.alias("Capture", Capture.class);
			magicApi.omitField(Capture.class, "regionImage");
			//magicApi.omitField(Capture.class, "classification");

			List<Capture> captures = (List<Capture>) magicApi.fromXML(reader);

			reader.close();

			return captures;

		} catch (IOException ioe) {
			warn("No capture file was found. A new capture file has been created!");
			
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(fileName, "UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer.println("<linked-list>");
			writer.println("</linked-list>");
			writer.close();
			return new ArrayList<Capture>();
			
		}
	}

	@Override
	public Mat getCaptureImage(int captureId)  {
		return Highgui.imread(getPath(captureId));
	}
	
	@Override
	public Mat getCaptureImage(Capture cap) {
		return Highgui.imread(getPath(cap.id)); 
	}

	public void saveConfiguration() {
		NoNameCoder nameCoder = new NoNameCoder(); 
		XStream magicApi = new XStream(new StaxDriver(nameCoder));
		magicApi.registerConverter(new MapEntryConverter());
		magicApi.alias("configuration", Map.class);

		String xml = magicApi.toXML(this.configuration);
		try {

			FileWriter xmlWriter = new FileWriter("configuration.xml");
			xmlWriter.write(xml);
			xmlWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ISubscription addEventHandler(EventType type,
			IEventHandler<LogEvent> handler) {
		return logEventDispatcher.addEventHandler(type, handler);
	}

	@Override
	public void error(String msg) {
		log("ERROR", msg);
	}

	@Override
	public void info(String msg) {
		log("INFO", msg);
	}

	@Override
	public void warn(String msg) {
		log("WARNING", msg);
	}

	public void log(String prio, String msg) {
		// Use log writer to store the log message
		try {
			String logEntry = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date()) + "\t" + prio + "\t" + msg.replace("\n","") + "\r\n";

			FileWriter logWriter = new FileWriter("system.log", true);
			logWriter.append(logEntry);
			logWriter.flush();
			logWriter.close();
			System.out.print(logEntry);

			// Dispatch log event
			EventType type;
			switch (prio) {
			case "ERROR":
				type = LogEvent.ERROR;
				break;
			case "WARN":
				type = LogEvent.WARN;
				break;
			default:
				type = LogEvent.INFO;
			}
			logEventDispatcher.dispatch(new LogEvent(type, logEntry));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getLog(Date startTime, Date endTime) {
		// function for getting logs from system.log file
		String fileName="system.log";
		File file = new File(fileName);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BufferedReader in = null;
		String log = "\n";

		try {
			in = new BufferedReader(new InputStreamReader(
					new ReverseLineInputStream(file)));
			String line = null;
			Date time = new Date(); // Start value set to now

			while (time.compareTo(startTime) > 0) {
				line = in.readLine();
				if (line == null) {
					break;
				}
				if (!line.isEmpty()){
					time = df.parse(line.split("\t")[0]);
					if (startTime.compareTo(time) * time.compareTo(endTime) > 0) {
						log = line + "\n" + log;
					}
				}
					

			}
			in.close();
		} catch (FileNotFoundException e) {
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(fileName, "UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			writer.close();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return log;
	}

	public String getLog(Date startTime) {
		// If no specified endTime we use NOW.
		Date endTime = new Date();
		return getLog(startTime, endTime);
	}

	@Override
	public void dispose() {

	}

	@Override
	public Object getConfigOption(String option) {
		return configuration.get(option);
	}

	@Override
	public void setConfigOption(String option, String value) {
		configuration.put(option, value);
		saveConfiguration();
	}

	private static class CaptureConverter implements Converter {

		@Override
		public boolean canConvert(Class clazz) {
			return Capture.class.isAssignableFrom(clazz);
		}

		@Override
		public void marshal(Object value, HierarchicalStreamWriter writer,
				MarshallingContext context) {
			Capture capture = (Capture) value;
			writer.startNode("Capture");
			writer.addAttribute("id", String.valueOf(capture.id));
			writer.addAttribute("longitude", String.valueOf(capture.longitude));
			writer.addAttribute("latitude", String.valueOf(capture.latitude));
			writer.addAttribute("timeStamp", new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(capture));
			writer.addAttribute("trapId", String.valueOf(capture.id));
			writer.endNode();
		}

		@Override
		public Object unmarshal(HierarchicalStreamReader reader,
				UnmarshallingContext context) {
			Capture capture = new Capture();

			while (reader.hasMoreChildren()) {
				reader.moveDown();
				try {
					capture.id = Integer.parseInt(reader
							.getAttribute("id"));
					capture.longitude = Double.parseDouble(reader.getAttribute("longitude"));
					capture.latitude = Double.parseDouble(reader.getAttribute("latitude"));
					capture.timeStamp = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss").parse(reader
							.getAttribute("timeStamp"));
					capture.trapDeviceId = Integer.parseInt(reader
							.getAttribute("trapId"));
				} catch (ParseException ex) {
					System.out.println("Error in FileRepository: "
							+ ex.getMessage());
				}
				reader.moveUp();
			}
			return capture;
		}

	}

	private class ReverseLineInputStream extends InputStream {

		RandomAccessFile in;

		long currentLineStart = -1;
		long currentLineEnd = -1;
		long currentPos = -1;
		long lastPosInFile = -1;

		public ReverseLineInputStream(File file) throws FileNotFoundException {
			in = new RandomAccessFile(file, "r");
			currentLineStart = file.length();
			currentLineEnd = file.length();
			lastPosInFile = file.length() - 1;
			currentPos = currentLineEnd;
		}

		public void findPrevLine() throws IOException {

			currentLineEnd = currentLineStart;

			// There are no more lines, since we are at the beginning of the
			// file and no lines.
			if (currentLineEnd == 0) {
				currentLineEnd = -1;
				currentLineStart = -1;
				currentPos = -1;
				return;
			}

			long filePointer = currentLineStart - 1;

			while (true) {
				filePointer--;

				// we are at start of file so this is the first line in the
				// file.
				if (filePointer < 0) {
					break;
				}

				in.seek(filePointer);
				int readByte = in.readByte();

				// We ignore last LF in file. search back to find the previous
				// LF.
				if (readByte == 0xA && filePointer != lastPosInFile) {
					break;
				}
			}
			// we want to start at pointer +1 so we are after the LF we found or
			// at 0 the start of the file.
			currentLineStart = filePointer + 1;
			currentPos = currentLineStart;
		}

		public int read() throws IOException {

			if (currentPos < currentLineEnd) {
				in.seek(currentPos++);
				int readByte = in.readByte();
				return readByte;

			} else if (currentPos < 0) {
				return -1;
			} else {
				findPrevLine();
				return read();
			}
		}
	}

}
