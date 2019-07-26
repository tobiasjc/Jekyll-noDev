package management;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.UnknownObjectException;

public class Environment {
	private HashMap<String, String> args;
	private int professorQuantity;
	private int classQuantity;
	private int dayQuantity;
	private int slotsQuantity;

	public Environment(HashMap<String, String> args) {
		this.args = args;
		populateNumbers();
	}

	public void populateNumbers() {
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(this.args.get("--inputPath")), StandardCharsets.UTF_8))) {

			String[] parts = br.readLine().split("\\t");

			this.professorQuantity = Integer.parseInt(parts[0]);

			parts = br.readLine().split("\\t");

			this.classQuantity = Integer.parseInt(parts[0]);

			parts = br.readLine().split("\\t");

			this.dayQuantity = Integer.parseInt(parts[0]);
			this.slotsQuantity = Integer.parseInt(parts[1]);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public boolean[][][] readImpossibilities() {
		boolean[][][] tmp = null;

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(this.args.get("--inputPath")), StandardCharsets.UTF_8))) {

			String[] parts = br.readLine().split("\\t");

			int teachersQuantity = Integer.parseInt(parts[0]);

			br.readLine().split("\\t");

			parts = br.readLine().split("\\t");

			int days = Integer.parseInt(parts[0]);
			int horarys = Integer.parseInt(parts[1]);

			tmp = new boolean[days][horarys][teachersQuantity];
			for (boolean[][] l : tmp) {
				for (boolean[] p : l) {
					Arrays.fill(p, false);
				}
			}

			String line;
			while ((line = br.readLine()) != null) {
				parts = line.split("\\t");
				if (parts[0].contentEquals("i")) {
					int day = Integer.parseInt(parts[1]);
					int horary = Integer.parseInt(parts[2]);
					int teacher = Integer.parseInt(parts[3]);

					tmp[day][horary][teacher] = true;
				}

			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return tmp;
	}

	public int[][] readObligations() {
		int[][] tmp = null;

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(this.args.get("--inputPath")), StandardCharsets.UTF_8))) {

			String[] parts = br.readLine().split("\\t");

			int teachersQuantity = Integer.parseInt(parts[0]);

			parts = br.readLine().split("\\t");

			int classes = Integer.parseInt(parts[0]);

			tmp = new int[teachersQuantity][classes];

			String line;
			while ((line = br.readLine()) != null) {
				parts = line.split("\\t");
				if (parts[0].contentEquals("a")) {
					int teacher = Integer.parseInt(parts[1]);
					int clss = Integer.parseInt(parts[2]);

					tmp[teacher][clss] = Integer.parseInt(parts[3]);
				}
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return tmp;
	}

	public void showProfessorSchedule(IloIntVar[][][][] x, IloCplex model, IloIntVar[][] q)
			throws UnknownObjectException, IloException {
		System.out.println("\nSchedule by professor.");
		for (int p = 0; p < this.professorQuantity; p++) {
			System.out.println("Professor " + p);
			System.out.print("\t");
			for (int k = 0; k < this.dayQuantity; k++) {
				System.out.print(DayOfWeek.of(k + 1).getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
				if (k != this.dayQuantity - 1) {
					System.out.print("\t");
				}
			}
			System.out.println();
			for (int s = 0; s < this.slotsQuantity; s++) {
				System.out.print("Slot " + s + ":\t");
				for (int d = 0; d < this.dayQuantity; d++) {
					for (int c = 0; c < this.classQuantity; c++) {
						if (model.getValue(x[p][d][c][s]) >= 0.5) {
							System.out.print(c);
						}
					}
					if (d != this.dayQuantity - 1) {
						System.out.print("\t");
					}
				}
				System.out.println();
			}
			System.out.print("Total:\t");
			for (int d = 0; d < this.dayQuantity; d++) {
				System.out.print(model.getValue(q[p][d]));
				if (d != this.dayQuantity - 1) {
					System.out.print("\t");
				}
			}
			System.out.println();
		}
	}

	public void showClassSchedule(IloIntVar[][][][] x, IloCplex model) throws UnknownObjectException, IloException {
		System.out.println("\nSchedule by class.");
		for (int c = 0; c < this.classQuantity; c++) {
			System.out.println("Class " + c);
			System.out.print("\t");
			for (int k = 0; k < this.dayQuantity; k++) {
				System.out.print(DayOfWeek.of(k + 1).getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
				if (k != this.dayQuantity - 1) {
					System.out.print("\t");
				}
			}
			System.out.println();
			for (int s = 0; s < this.slotsQuantity; s++) {
				System.out.print("Slot " + s + ":\t");
				for (int d = 0; d < this.dayQuantity; d++) {
					for (int p = 0; p < this.professorQuantity; p++) {
						if (model.getValue(x[p][d][c][s]) >= 0.5) {
							System.out.print(p);
						}
					}
					if (d != this.dayQuantity - 1) {
						System.out.print("\t");
					}
				}
				System.out.println();
			}
			System.out.println("\n");
		}
	}

	public int getClassQuantity() {
		return classQuantity;
	}

	public int getDayQuantity() {
		return dayQuantity;
	}

	public int getProfessorQuantity() {
		return professorQuantity;
	}

	public int getSlotQuantity() {
		return slotsQuantity;
	}

}
