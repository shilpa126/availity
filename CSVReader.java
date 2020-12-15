package csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class CSVReader {

	class Enrollment {
		private String userId;
		private String name;
		private int version;
		private String insurance;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}

		public String getInsurance() {
			return insurance;
		}

		public void setInsurance(String insurance) {
			this.insurance = insurance;
		}

		@Override
		public String toString() {
			return getUserId() + "," + getName() + "," + getVersion() + "," + getInsurance();
		}

	}

	private ArrayList<Enrollment> readFile() {
		URL path = CSVReader.class.getResource("availity_enrollment.csv");
		BufferedReader reader;
		ArrayList<Enrollment> enrollments = new ArrayList<Enrollment>();
		try {
			File f = new File(path.getFile());
			reader = new BufferedReader(new FileReader(f));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] split = line.split(",\\s*");
				Enrollment temp = new Enrollment();
				temp.setUserId(split[0].trim());
				temp.setName(split[1].trim());
				temp.setVersion(Integer.parseInt(split[2].trim()));
				temp.setInsurance(split[3].trim());
				enrollments.add(temp);
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return enrollments;
	}

	@SuppressWarnings("serial")
	private void processEnrollments(ArrayList<Enrollment> enrollments) {
		HashMap<String, ArrayList<Enrollment>> map = new HashMap<String, ArrayList<Enrollment>>();
		enrollments.forEach(e -> {
			if (map.containsKey(e.getInsurance())) {
				map.get(e.getInsurance()).add(e);
			} else {
				map.put(e.getInsurance(), new ArrayList<Enrollment>() {
					{
						add(e);
					}
				});
			}
		});

		map.keySet().forEach(key -> {
			sortAndSaveFile(map.get(key));
		});
	}

	private void sortAndSaveFile(ArrayList<Enrollment> enrollments) {
		enrollments.sort(Comparator.comparing(Enrollment::getName).thenComparing(Enrollment::getVersion));
	
		try {
			FileWriter writer = new FileWriter(enrollments.get(0).getInsurance() + ".csv");
			for (int i = 0; i < enrollments.size(); i++) {
				if (i + 1 < enrollments.size()
						&& enrollments.get(i).getUserId().equalsIgnoreCase(enrollments.get(i + 1).getUserId())) {
					continue;
				} else {
					writer.write(enrollments.get(i).toString());
					writer.write("\n");
				}
			}

			writer.close();
			System.out.println("Successfully wrote to the file " + enrollments.get(0).getInsurance() + ".csv");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		CSVReader reader = new CSVReader();
		ArrayList<Enrollment> enrollments = reader.readFile();
		reader.processEnrollments(enrollments);
	}

}
