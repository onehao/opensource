package com.onehao.acts.testgen.service.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.common.TestGenProfile;
import com.onehao.acts.testgen.common.TestSet;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.util.Util;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public class ForbesEngine {
    private ArrayList<Parameter> params;
    private TestSet ts;
    static int t;
    static int k;
    static int[] v;
    static int[] vp;
    static int vmax;
    static int[] vmaxi;
    static int tp;
    static int start_c;
    static int[][] ca;
    static int r;
    static int c;
    static int good_r;
    static int good_c;
    static int[] g;
    static int[] end_pack;
    static long prev_seed = 0L;
    static long cur_seed = 0L;
    static Random rng = new Random();
    static long h_time = 0L;
    static long v_time = 0L;
    static long cur_time;
    static long start_time;
    static int add_row_count = 0;
    static int flags = 0;
    static boolean[][][] cov;
    static long[][] t_c;
    static int[] ca_maxrow;
    static boolean[][] cov_maxval;
    static long[] t_c_maxval;
    static int[] other_row;
    static int[] shared_cols;
    static int num_scols;
    static int[] ca_cols;

    public ForbesEngine(SUT sut, int flags) {
        ForbesEngine.flags = flags;
        this.params = Util.orderParams(sut.getParams());
        t = TestGenProfile.instance().getDOI();
        this.ts = new TestSet(this.params, sut.getOutputParameters());

        start_time = System.currentTimeMillis();

        tp = t - 1;
        v = new int[this.params.size()];
        vp = new int[this.params.size()];
        for (int i = 0; i < this.params.size(); i++) {
            v[i] = ((Parameter) this.params.get(i)).getDomainSize();
            v[i] -= 1;
        }
        k = v.length;
        vmax = v[0];
        vmaxi = new int[t + 1];
        vmaxi[0] = 1;
        for (int i = 1; i <= t; i++) {
            vmaxi[i] = (vmax * vmaxi[(i - 1)]);
        }
        cur_seed = rng.nextLong();
        rng.setSeed(cur_seed);

        System.out.println("Seed: " + cur_seed);

        ForbesMath.initialize(k, t);

        int init_rows = (int) Math.pow(vmax, t) * (int) (Math.log(k) + 1.0D) * 2;
        ca = new int[init_rows][k];
        g = new int[init_rows];
        shared_cols = new int[k];
        ca_cols = new int[k];
        end_pack = new int[k];
    }

    public TestSet getTestSet() {
        return this.ts;
    }

    public void build() {
        r = 1;
        for (int i = 0; i < t; i++) {
            r *= v[i];
        }
        c = t;
        for (int i = 0; i < t; i++) {
            int repeat = 1;
            int repeat2 = 1;
            for (int j = i + 1; j < t; j++) {
                repeat *= v[j];
            }
            for (int j = 0; j < i; j++) {
                repeat2 *= v[j];
            }
            for (int m = 0; m < repeat2; m++) {
                for (int j = 0; j < v[i]; j++) {
                    for (int l = 0; l < repeat; l++) {
                        ca[(v[i] * m * repeat + repeat * j + l)][i] = j;
                    }
                }
            }
            end_pack[i] = r;
        }
        for (int i = 0; i < r; i++) {
            g[i] = c;
        }
        for (int i = 0; i < c; i++) {
            System.out.println("Covering parameter: " + this.ts.getParam(i).getName());
        }
        for (int cols = c; cols < k; cols++) {
            cur_time = System.currentTimeMillis();
            if ((flags & 0x8) != 8) {
                ipo_h();
            } else {
                ipo_h_v2();
            }
            prev_seed = cur_seed;
            cur_seed = rng.nextLong();
            rng.setSeed(cur_seed);

            h_time += System.currentTimeMillis() - cur_time;
            cur_time = System.currentTimeMillis();

            ipo_v();

            v_time += System.currentTimeMillis() - cur_time;
        }
        System.out.println();

        System.out.println("CA(t;v_str)=CA(" + t + ";" + v_str(c) + ")=" + r);
        System.out.print("Time: " + (System.currentTimeMillis() - start_time) / 1000.0D + " s");
        System.out.println(", (" + h_time + "," + v_time + ") ms");
        System.out.println("Added rows " + add_row_count + " times.");

        ArrayList<int[]> matrix = new ArrayList<int[]>(r);
        for (int i = 0; i < r; i++) {
            matrix.add(ca[i]);
        }
        this.ts.addMatrix(matrix);
    }

    static int max_row = 0;
    static int max_value = 0;
    static long max_covered = 0L;
    static int umrow;
    static int[] tuple_numbers;
    static int[] dont_cares;
    static int[] columns;
    static int old_r;
    static int vn;
    static boolean placed;
    static boolean good_row;
    static boolean[] coverage;
    static int num_ut_ced;
    static int start_unpack;
    static int[] value_tuple;
    static int[] focus;
    static boolean[] orient;

    private static void ipo_h() {
        cov = new boolean[v[c]][vmaxi[tp]][ForbesMath.binom(c, tp)];
        t_c = new long[v[c]][r];
        boolean[] marked = new boolean[r];

        List<ForbesPair> rvpairs = new LinkedList<ForbesPair>();
        for (int i = 0; i < r; i++) {
            ca[i][c] = -1;
        }
        for (int rows = 0; rows < r; rows++) {
            if (rvpairs.size() != 0) {
                for (ListIterator<ForbesPair> rvpair_iterator = rvpairs.listIterator(); rvpair_iterator.hasNext();) {
                    ForbesPair rvpair = (ForbesPair) rvpair_iterator.next();
                    if (rvpair.first == max_row) {
                        rvpair_iterator.remove();
                    } else if (rvpair.second == max_value) {
                        long covered = ForbesMath.binom(g[rvpair.first], tp) - t_c[rvpair.second][rvpair.first];
                        if (covered < max_covered) {
                            rvpair_iterator.remove();
                        }
                    }
                }
            }
            if (rvpairs.size() == 0) {
                max_row = 0;
                max_value = 0;
                max_covered = 0L;
                boolean set = false;
                for (int row = 0; row < r; row++) {
                    // TODO: onehao marked[row] == 0 -> marked[row] == false
                    if (marked[row] == false) {
                        for (int i = 0; i < v[c]; i++) {
                            long covered = ForbesMath.binom(g[row], tp) - t_c[i][row];
                            if (covered > max_covered) {
                                rvpairs.clear();
                            }
                            if ((covered >= max_covered) || (!set)) {
                                rvpairs.add(new ForbesPair(row, i));
                                max_covered = covered;
                                set = true;
                            }
                        }
                    }
                }
            }
            if (max_covered == 0L) {
                break;
            }
            Iterator<ForbesPair> max_pair_iterator = rvpairs.iterator();
            for (int i = 0; i < rng.nextInt(rvpairs.size()); i++) {
                max_pair_iterator.next();
            }
            ForbesPair max_pair = (ForbesPair) max_pair_iterator.next();
            max_row = max_pair.first;
            max_value = max_pair.second;

            mark(max_row, c, max_value);
            marked[max_row] = true;

            ca_maxrow = ca[max_row];
            cov_maxval = cov[max_value];
            t_c_maxval = t_c[max_value];
            for (umrow = 0; umrow < r; umrow += 1) {

                // TODO: onehao, marked[umrow] == 0 -> marked[umrow] == false.
                if (marked[umrow] == false) {
                    other_row = ca[umrow];

                    num_scols = 0;
                    for (int i = 0; i < c; i++) {
                        if ((other_row[i] == ca_maxrow[i]) && (ca_maxrow[i] != -1)) {
                            shared_cols[num_scols] = i;
                            ca_cols[num_scols] = ca_maxrow[i];
                            num_scols += 1;
                        }
                    }
                    if (num_scols >= tp) {
                        shared_update(num_scols, tp, 0, 0);
                    }
                }
            }
            num_scols = 0;
            for (int i = 0; i < c; i++) {
                if (ca_maxrow[i] != -1) {
                    shared_cols[num_scols] = i;
                    ca_cols[num_scols] = ca_maxrow[i];
                    num_scols += 1;
                }
            }
            if (num_scols >= tp) {
                coverage_update(num_scols, tp, 0, 0);
            }
        }
        c += 1;
        if (c > k) {
            throw new StackOverflowError();
        }
    }

    static void shared_update(int start, int pos, int tuple_number, int column_number) {
        tuple_number *= vmax;
        int posp = pos - 1;
        if (pos > 1) {
            for (int i = posp; i < start; i++) {
                shared_update(i, posp, tuple_number + ca_cols[i], column_number + ForbesMath.binom(shared_cols[i], pos));
            }
        } else {
            for (int i = 0; i < start; i++) {
                // TODO: cov_maxval[(tuple_number + ca_cols[i])][(column_number + shared_cols[i])] == 0 ->
                // cov_maxval[(tuple_number + ca_cols[i])][(column_number + shared_cols[i])] == false
                if (cov_maxval[(tuple_number + ca_cols[i])][(column_number + shared_cols[i])] == false) {
                    t_c_maxval[umrow] += 1L;
                }
            }
        }
    }

    static void coverage_update(int start, int pos, int tuple_number, int column_number) {
        tuple_number *= vmax;
        int posp = pos - 1;
        if (pos > 1) {
            for (int i = posp; i < start; i++) {
                coverage_update(i, posp, tuple_number + ca_cols[i],
                        column_number + ForbesMath.binom(shared_cols[i], pos));
            }
        } else {
            for (int i = 0; i < start; i++) {
                // TODO: onehao, cov_maxval[(tuple_number + ca_cols[i])][(column_number + shared_cols[i])] == 1
                // cov_maxval[(tuple_number + ca_cols[i])][(column_number + shared_cols[i])] == true
                cov_maxval[(tuple_number + ca_cols[i])][(column_number + shared_cols[i])] = true;
            }
        }
    }

    private static void ipo_h_v2() {
        t_c = new long[v[c]][r];
        boolean[] marked = new boolean[r];

        List<ForbesPair> rvpairs = new LinkedList<ForbesPair>();
        for (int i = 0; i < r; i++) {
            ca[i][c] = -1;
        }
        for (int rows = 0; rows < r; rows++) {
            if (rvpairs.size() != 0) {
                for (ListIterator<ForbesPair> rvpair_iterator = rvpairs.listIterator(); rvpair_iterator.hasNext();) {
                    ForbesPair rvpair = (ForbesPair) rvpair_iterator.next();
                    if (rvpair.first == max_row) {
                        rvpair_iterator.remove();
                    } else if (rvpair.second == max_value) {
                        long covered = ForbesMath.binom(g[rvpair.first], tp) - t_c[rvpair.second][rvpair.first];
                        if (covered < max_covered) {
                            rvpair_iterator.remove();
                        }
                    }
                }
            }
            if (rvpairs.size() == 0) {
                max_row = 0;
                max_value = 0;
                max_covered = 0L;
                boolean set = false;
                for (int row = 0; row < r; row++) {
                    // TODO: onehao, marked[row] == 0 -> marked[row] == false.
                    if (marked[row] == false) {
                        for (int i = 0; i < v[c]; i++) {
                            long covered = ForbesMath.binom(g[row], tp) - t_c[i][row];
                            if (covered > max_covered) {
                                rvpairs.clear();
                            }
                            if ((covered >= max_covered) || (!set)) {
                                rvpairs.add(new ForbesPair(row, i));
                                max_covered = covered;
                                set = true;
                            }
                        }
                    }
                }
            }
            if (rvpairs.size() == 0) {
                break;
            }
            Iterator<ForbesPair> max_pair_iterator = rvpairs.iterator();
            for (int i = 0; i < rng.nextInt(rvpairs.size()); i++) {
                max_pair_iterator.next();
            }
            ForbesPair max_pair = (ForbesPair) max_pair_iterator.next();
            max_row = max_pair.first;
            max_value = max_pair.second;
            mark(max_row, c, max_value);
            marked[max_row] = true;

            ca_maxrow = ca[max_row];
            t_c_maxval = t_c[max_value];
            for (umrow = 0; umrow < r; umrow += 1) {
                // TODO: onehao, marked[umrow] == 0 -> marked[umrow] == false
                if (marked[umrow] == false) {
                    other_row = ca[umrow];
                    num_scols = 0;
                    for (int i = 0; i < c; i++) {
                        if ((other_row[i] == ca_maxrow[i]) && (ca_maxrow[i] != -1)) {
                            num_scols += 1;
                        }
                    }
                    if (num_scols >= tp) {
                        t_c_maxval[umrow] += ForbesMath.binom(num_scols, tp);
                    }
                }
            }
        }
        c += 1;
        if (c > k) {
            throw new StackOverflowError();
        }
    }

    static void ipo_v() {
        old_r = r;
        tuple_numbers = new int[old_r];
        dont_cares = new int[old_r];
        columns = new int[t];
        coverage = new boolean[vmaxi[t]];
        value_tuple = new int[t + 1];
        focus = new int[t + 1];
        orient = new boolean[t + 1];
        for (int i = 0; i < c; i++) {
            while ((end_pack[i] < old_r) && (ca[end_pack[i]][i] != -1)) {
                end_pack[i] += 1;
            }
        }
        for (int i = 0; i < old_r; i++) {
            if (ca[i][(c - 1)] == -1) {
                dont_cares[i] += 1;
            } else {
                tuple_numbers[i] = ca[i][(c - 1)];
            }
        }
        columns[tp] = (c - 1);
        tuple_place(c - 1, tp);
    }

    static void tuple_place(int start, int pos) {
        int posp = pos - 1;
        if (pos > 1) {
            for (int i = posp; i < start; i++) {
                for (int j = 0; j < old_r; j++) {
                    if (ca[j][i] == -1) {
                        dont_cares[j] += 1;
                    } else if (dont_cares[j] == 0) {
                        tuple_numbers[j] = (tuple_numbers[j] * vmax + ca[j][i]);
                    }
                }
                columns[posp] = i;
                tuple_place(i, posp);
                for (int j = 0; j < old_r; j++) {
                    if (ca[j][i] == -1) {
                        dont_cares[j] -= 1;
                    } else if (dont_cares[j] == 0) {
                        tuple_numbers[j] = ((tuple_numbers[j] - ca[j][i]) / vmax);
                    }
                }
            }
        } else {
            for (int i = 0; i < start; i++) {
                columns[0] = i;
                num_ut_ced = 1;
                for (int m = 0; m < t; m++) {
                    num_ut_ced *= v[columns[m]];
                }
                Arrays.fill(coverage, false);
                for (int j = 0; j < old_r; j++) {
                    if ((dont_cares[j] == 0) && (ca[j][i] != -1)
                    // TODO: onehao, coverage[(tuple_numbers[j] * vmax + ca[j][i])] == 0 -> coverage[(tuple_numbers[j] *
                    // vmax + ca[j][i])] == false
                            && (coverage[(tuple_numbers[j] * vmax + ca[j][i])] == false)) {
                        coverage[(tuple_numbers[j] * vmax + ca[j][i])] = true;
                        num_ut_ced -= 1;
                    }
                }
                if (num_ut_ced != 0) {
                    start_unpack = end_pack[columns[0]];
                    for (int j = 1; j < t; j++) {
                        start_unpack = Math.min(start_unpack, end_pack[columns[j]]);
                    }
                    Arrays.fill(value_tuple, 0);
                    Arrays.fill(orient, true);
                    for (int j = 0; j <= t; j++) {
                        focus[j] = j;
                    }
                    vn = 0;
                    for (;;) {
                        // coverage[vn] == 0 -> coverage[vn] == false.
                        if (coverage[vn] == false) {
                            placed = false;
                            for (int l = start_unpack; (l < old_r) && (!placed); l++) {
                                if ((dont_cares[l] != 0) || (ca[l][columns[0]] == -1)) {
                                    good_row = true;
                                    for (int m = 0; (m < t) && (good_row); m++) {
                                        good_row =
                                                (good_row)
                                                        && ((ca[l][columns[m]] == value_tuple[m]) || (ca[l][columns[m]] == -1));
                                    }
                                    if (good_row) {
                                        placed = true;
                                        if (ca[l][i] == -1) {
                                            ca[l][i] = value_tuple[0];
                                            g[l] += 1;
                                        }
                                        for (int m = 1; m < t; m++) {
                                            if (ca[l][columns[m]] == -1) {
                                                ca[l][columns[m]] = value_tuple[m];
                                                g[l] += 1;
                                                dont_cares[l] -= 1;
                                            }
                                        }
                                        tuple_numbers[l] = ((vn - ca[l][i]) / vmax);
                                    }
                                }
                            }
                            for (int l = old_r; (l < r) && (!placed); l++) {
                                good_row = true;
                                for (int m = 0; (m < t) && (good_row); m++) {
                                    good_row =
                                            (good_row)
                                                    && ((ca[l][columns[m]] == value_tuple[m]) || (ca[l][columns[m]] == -1));
                                }
                                if (good_row) {
                                    placed = true;
                                    for (int m = 0; m < t; m++) {
                                        mark(l, columns[m], value_tuple[m]);
                                    }
                                }
                            }
                            if (!placed) {
                                r += 1;
                                if (r > ca.length) {
                                    add_row();
                                }
                                for (int m = 0; m < c; m++) {
                                    ca[(r - 1)][m] = -1;
                                }
                                g[(r - 1)] = 0;

                                placed = true;
                                for (int m = 0; m < t; m++) {
                                    mark(r - 1, columns[m], value_tuple[m]);
                                }
                            }
                        }
                        int m = focus[0];
                        focus[0] = 0;

                        // TODO: onehao,orient[m] != 0 - > orient[m]
                        if (orient[m]) {
                            value_tuple[m] += 1;
                        } else {
                            value_tuple[m] -= 1;
                        }
                        if (m == t) {
                            break;
                        }
                        // TODO: onehao, orient[m] != 0 -> orient[m]
                        if (orient[m]) {
                            vn += vmaxi[m];
                        } else {
                            vn -= vmaxi[m];
                        }
                        if ((value_tuple[m] == 0) || (value_tuple[m] == vp[columns[m]])) {
                            // TODO: onehao, orient[m] != 0 ? 0 : true -> orient[m]? false : true
                            orient[m] = (orient[m] ? false : true);
                            focus[m] = focus[(m + 1)];
                            focus[(m + 1)] = (m + 1);
                        }
                    }
                }
            }
        }
    }

    private static void mark(int row, int col, int value) {
        if (ca[row][col] == -1) {
            ca[row][col] = value;
            g[row] += 1;
        }
    }

    private static void add_row() {
        add_row_count += 1;

        int[][] ca_new = new int[ca.length + (int) Math.pow(vmax, t) * (int) (Math.log(k) + 1.0D)][];
        for (int i = 0; i < ca.length; i++) {
            ca_new[i] = ca[i];
        }
        for (int i = ca.length; i < ca_new.length; i++) {
            ca_new[i] = new int[k];
        }
        ca = ca_new;

        int[] g_new = new int[ca.length];
        for (int i = 0; i < g.length; i++) {
            g_new[i] = g[i];
        }
        g = g_new;
    }

    public static String v_str(int cols) {
        int old_v = v[0];
        int repeats = 1;
        String v_str = new String();
        for (int i = 1; i < cols; i++) {
            if (v[i] == old_v) {
                repeats++;
            } else {
                if (repeats == 1) {
                    v_str = v_str + old_v + ",";
                } else {
                    v_str = v_str + old_v + "^" + repeats + ",";
                }
                old_v = v[i];
                repeats = 1;
            }
        }
        if (repeats == 1) {
            v_str = v_str + old_v;
        } else {
            v_str = v_str + old_v + "^" + repeats;
        }
        return v_str;
    }
}
