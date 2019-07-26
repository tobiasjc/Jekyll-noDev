package management;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloObjective;
import ilog.cplex.IloCplex;

public class Constructor {
	private static int P;
	private static int D;
	private static int C;
	private static int S;

	public Constructor(int p, int d, int c, int s) {
		P = p;
		D = d;
		C = c;
		S = s;
	}

	public void setInstanceParameters(IloCplex model, IloIntVar[][][][] x, boolean[][][] i, int[][] o, IloIntVar[][] q)
			throws IloException {
		// create decision variables
		for (int p = 0; p < x.length; p++) {
			for (int d = 0; d < x[p].length; d++) {
				for (int c = 0; c < x[p][d].length; c++) {
					for (int s = 0; s < x[p][d][c].length; s++) {
						x[p][d][c][s] = model.boolVar("X[" + p + ", " + d + ", " + c + ", " + s + "]");
					}
				}
			}
		}

		// add the impossibilities to the model
		for (int d = 0; d < D; d++) {
			for (int p = 0; p < P; p++) {
				for (int s = 0; s < S; s++) {
					if (i[d][s][p]) {
						IloLinearIntExpr r = model.linearIntExpr();
						for (int c = 0; c < C; c++) {
							r.addTerm(1, x[p][d][c][s]);
						}
						model.addEq(r, 0);
					}
				}
			}
		}

		// add the obligations to the model
		for (int p = 0; p < o.length; p++) {
			for (int c = 0; c < o[p].length; c++) {
				IloLinearIntExpr r = model.linearIntExpr();
				for (int d = 0; d < x[p].length; d++) {
					for (int s = 0; s < x[p][d][c].length; s++) {
						r.addTerm(1, x[p][d][c][s]);
					}
				}
				model.addEq(r, o[p][c]);
			}
		}

		// only one teacher in one class in a single time...
		for (int c = 0; c < C; c++) {
			for (int s = 0; s < S; s++) {
				for (int d = 0; d < D; d++) {
					IloLinearIntExpr r = model.linearIntExpr();
					for (int p = 0; p < P; p++) {
						r.addTerm(1, x[p][d][c][s]);
					}
					model.addLe(r, 1);
				}
			}
		}

		// ...and only one professor in the class at a time;
		for (int p = 0; p < P; p++) {
			for (int d = 0; d < D; d++) {
				for (int s = 0; s < S; s++) {
					IloLinearIntExpr r = model.linearIntExpr();
					for (int c = 0; c < C; c++) {
						r.addTerm(1, x[p][d][c][s]);
					}
					model.addLe(r, 1);
				}
			}
		}

		// set 'q' as the sum of classes from a professor in a day
		for (int p = 0; p < P; p++) {
			for (int d = 0; d < D; d++) {
				IloLinearIntExpr r = model.linearIntExpr();
				for (int c = 0; c < C; c++) {
					for (int s = 0; s < S; s++) {
						r.addTerm(1, x[p][d][c][s]);
					}
				}
				q[p][d] = model.intVar(0, 6, "Q[" + p + ", " + d + "]");
				model.addEq(r, q[p][d]);
			}
		}
	}

	public void restrictTrippleSlots(IloCplex model, IloIntVar[][][][] x) throws IloException {
		// says that: 'one professor, in one day and inside a class, can only be
		// allocated on a maximum of 2 slots'
		for (int d = 0; d < D; d++) {
			for (int p = 0; p < P; p++) {
				for (int c = 0; c < C; c++) {
					IloLinearIntExpr r = model.linearIntExpr();
					for (int s = 0; s < S; s++) {
						r.addTerm(1, x[p][d][c][s]);
					}
					model.addLe(r, 2);
				}
			}
		}
	}

	public void setTwined(IloCplex model, IloIntVar[][][][] x, IloObjective z) throws IloException {
		IloIntVar[][][][] g = new IloIntVar[P][D][C][S / 2];
		// instantiate auxiliary twined slots variables adding terms to be part of the
		// objective
		IloLinearIntExpr gs = model.linearIntExpr();
		for (int p = 0; p < P; p++) {
			for (int d = 0; d < D; d++) {
				for (int c = 0; c < C; c++) {
					for (int s = 0; s < S / 2; s++) {
						g[p][d][c][s] = model.intVar(-5, 1, "G[" + p + ", " + d + ", " + c + ", " + s + "]");
						gs.addTerm(1, g[p][d][c][s]);
					}
				}
			}
		}

		// connect 'x' values, conditioning on 'g' values
		for (int p = 0; p < P; p++) {
			for (int d = 0; d < D; d++) {
				for (int c = 0; c < C; c++) {
					for (int s = 0, k = 0; s < S; s += 2, k++) {
						IloLinearIntExpr r = model.linearIntExpr();
						r.addTerm(1, x[p][d][c][s]);
						r.addTerm(1, x[p][d][c][s + 1]);
						model.add(model.ifThen(model.eq(r, 2), model.eq(g[p][d][c][k], -5)));
						model.add(model.ifThen(model.eq(r, 1), model.eq(g[p][d][c][k], +1)));
					}
				}
			}
		}

		// add the objective to the objective function
		model.addToExpr(z, gs);
	}

	public void setIsolated(IloCplex model, IloIntVar[][][][] x, IloObjective z) throws IloException {
		IloIntVar[][] y = new IloIntVar[P][D];
		// instantiate auxiliary window variables adding terms to be part of the[
		// objective
		IloLinearIntExpr ys = model.linearIntExpr();
		for (int p = 0; p < P; p++) {
			for (int d = 0; d < D; d++) {
				y[p][d] = model.boolVar("Y[" + p + ", " + d + "]");
				ys.addTerm(1, y[p][d]);
			}
		}

		// connect 'x' values, conditioning on 'y' values
		for (int p = 0; p < P; p++) {
			for (int d = 0; d < D; d++) {
				IloLinearIntExpr r = model.linearIntExpr();
				for (int c = 0; c < C; c++) {
					for (int s = 0; s < S; s++) {
						r.addTerm(1, x[p][d][c][s]);
					}
				}
				model.add(model.ifThen(model.eq(r, 0), model.eq(y[p][d], 0)));
				model.add(model.ifThen(model.eq(r, 1), model.eq(y[p][d], 1)));
				model.add(model.ifThen(model.ge(r, 2), model.eq(y[p][d], 0)));
			}
		}

		// add the objective to the objective function
		model.addToExpr(z, ys);
	}

	public void setWindows(IloCplex model, IloIntVar[][][][] x, IloObjective z, IloIntVar[][] q) throws IloException {
		IloIntVar[][] t = new IloIntVar[D][P];
		// instantiate auxiliary window variables adding terms to be part of the
		// objective
		for (int p = 0; p < P; p++) {
			for (int d = 0; d < D; d++) {
				t[d][p] = model.intVar(0, 10, "T[" + d + ", " + p + "]");
			}
		}

		for (int p = 0; p < P; p++) {
			for (int d = 0; d < D; d++) {
				int lst = -1;
				int fst = 9;
				for (int s = 0; s < S; s++) {
					for (int c = 0; c < C; c++) {
						for (int _s = 0; _s < S; _s++) {

						}
					}
				}
			}
		}

		// add the objective to the objective function
//		model.addToExpr(z, ts);
	}
}
