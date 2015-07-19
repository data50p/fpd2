package com.femtioprocent.fpd2.dhry;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Dhry extends GlobalVariables implements Runnable {

    private static String f(long n) {
	return "" + n;
    }

    private static String f(int n) {
	return f((long) n);
    }

    long Number_Of_Runs = 100000000;

    ExitObserver exitObserver;

    public long execute(long num_of_runs) {
	try {
	    return Number_Of_Runs;
	} finally {
	    Number_Of_Runs = num_of_runs;
	    execute();
	}
    }

    public String execute() {

	int Int_Loc_1,
		Int_Loc_2,
		Int_Loc_3;
	int[] Int_Loc_3_Ref = new int[1];
	int[] Int_Loc_1_Ref = new int[1];
	char Char_Index;
	int[] Enum_Loc = new int[1];
	String String_Loc_1,
		String_Loc_2;

	long begin_time,
		end_time,
		total_time;

	int Run_Index,
		Meas_Index;

	Next_Record_Glob = Second_Record;
	Record_Glob = First_Record;

	Record_Glob.Record_Comp = Next_Record_Glob;
	Record_Glob.Discr = Ident_1;
	Record_Glob.Enum_Comp = Ident_3;
	Record_Glob.Int_Comp = 40;
	Record_Glob.String_Comp = "DHRYSTONE PROGRAM, SOME STRING";

	String_Loc_1 = "DHRYSTONE PROGRAM, 1'ST STRING";

	begin_time = System.currentTimeMillis();

	for (Run_Index = 1; Run_Index <= Number_Of_Runs; ++Run_Index) {

	    Proc_5();
	    Proc_4();

	    Int_Loc_1 = 2;
	    Int_Loc_2 = 3;

	    String_Loc_2 = "DHRYSTONE PROGRAM, 2'ND STRING";

	    Enum_Loc[0] = Ident_2;
	    Bool_Glob = !Func_2(String_Loc_1, String_Loc_2);

	    while (Int_Loc_1 < Int_Loc_2) {
		Int_Loc_3_Ref[0] = 5 * Int_Loc_1 - Int_Loc_2;
		Proc_7(Int_Loc_1, Int_Loc_2, Int_Loc_3_Ref);
		Int_Loc_1 += 1;
	    }

	    Int_Loc_3 = Int_Loc_3_Ref[0];
	    Proc_8(Array_Glob_1, Array_Glob_2, Int_Loc_1, Int_Loc_3);
	    Proc_1(Record_Glob);

	    for (Char_Index = 'A'; Char_Index <= Char_Glob_2; ++Char_Index) {
		if (Enum_Loc[0] == Func_1(Char_Index, 'C')) {
		    Proc_6(Ident_1, Enum_Loc);
		}
	    }

	    Int_Loc_3 = Int_Loc_2 * Int_Loc_1;
	    Int_Loc_2 = Int_Loc_3 / Int_Loc_1;
	    Int_Loc_2 = 7 * (Int_Loc_3 - Int_Loc_2) - Int_Loc_1;

	    Int_Loc_1_Ref[0] = Int_Loc_1;
	    Proc_2(Int_Loc_1_Ref);
	    Int_Loc_1 = Int_Loc_1_Ref[0];

	}

	end_time = System.currentTimeMillis();
	total_time = end_time - begin_time;
	if (total_time == 0) {
	    total_time = 1;
	}
	return "Total time: " + total_time + "ms\n" + "Result: " + Number_Of_Runs * 1000 / total_time
		+ " dhrystone/sec.\nMiljons: " + "    " + (Number_Of_Runs * 0.001 / total_time);

    }

    void Proc_1(Record_Type Pointer_Par_Val) {

	Record_Type Next_Record = Pointer_Par_Val.Record_Comp;

	Pointer_Par_Val.Record_Comp = Record_Glob;

	Pointer_Par_Val.Int_Comp = 5;

	Next_Record.Int_Comp = Pointer_Par_Val.Int_Comp;
	Next_Record.Record_Comp = Pointer_Par_Val.Record_Comp;
	Proc_3(Next_Record.Record_Comp);

	int[] Int_Ref = new int[1];

	if (Next_Record.Discr == Ident_1) {
	    Next_Record.Int_Comp = 6;
	    Int_Ref[0] = Next_Record.Enum_Comp;
	    Proc_6(Pointer_Par_Val.Enum_Comp, Int_Ref);
	    Next_Record.Enum_Comp = Int_Ref[0];
	    Next_Record.Record_Comp = Record_Glob.Record_Comp;
	    Int_Ref[0] = Next_Record.Int_Comp;
	    Proc_7(Next_Record.Int_Comp, 10, Int_Ref);
	    Next_Record.Int_Comp = Int_Ref[0];
	} else {
	    Pointer_Par_Val = Pointer_Par_Val.Record_Comp;
	}

    }

    void Proc_2(int Int_Par_Ref[]) {

	int Int_Loc;
	int Enum_Loc;

	Int_Loc = Int_Par_Ref[0] + 10;
	Enum_Loc = 0;

	do {
	    if (Char_Glob_1 == 'A') {
		Int_Loc -= 1;
		Int_Par_Ref[0] = Int_Loc - Int_Glob;
		Enum_Loc = Ident_1;
	    }
	} while (Enum_Loc != Ident_1);

    }

    void Proc_3(Record_Type Pointer_Par_Ref) {

	if (Record_Glob != null) {
	    Pointer_Par_Ref = Record_Glob.Record_Comp;
	} else {
	    Int_Glob = 100;
	}

	int[] Int_Comp_Ref = new int[1];
	Int_Comp_Ref[0] = Record_Glob.Int_Comp;
	Proc_7(10, Int_Glob, Int_Comp_Ref);
	Record_Glob.Int_Comp = Int_Comp_Ref[0];

    }

    void Proc_4() {

	boolean Bool_Loc;

	Bool_Loc = Char_Glob_1 == 'A';
	Bool_Loc = Bool_Loc || Bool_Glob;
	Char_Glob_2 = 'B';

    }

    void Proc_5() {

	Char_Glob_1 = 'A';
	Bool_Glob = false;

    }

    void Proc_6(int Enum_Par_Val, int Enum_Par_Ref[]) {

	Enum_Par_Ref[0] = Enum_Par_Val;

	if (!Func_3(Enum_Par_Val)) {
	    Enum_Par_Ref[0] = Ident_4;
	}

	switch (Enum_Par_Val) {

	    case Ident_1:
		Enum_Par_Ref[0] = Ident_1;
		break;

	    case Ident_2:
		if (Int_Glob > 100) {
		    Enum_Par_Ref[0] = Ident_1;
		} else {
		    Enum_Par_Ref[0] = Ident_4;
		}
		break;

	    case Ident_3:
		Enum_Par_Ref[0] = Ident_2;
		break;

	    case Ident_4:
		break;

	    case Ident_5:
		Enum_Par_Ref[0] = Ident_3;
		break;

	}

    }

    void Proc_7(int Int_Par_Val1, int Int_Par_Val2, int Int_Par_Ref[]) {

	int Int_Loc;

	Int_Loc = Int_Par_Val1 + 2;
	Int_Par_Ref[0] = Int_Par_Val2 + Int_Loc;

    }

    void Proc_8(int[] Array_Par_1_Ref, int[][] Array_Par_2_Ref, int Int_Par_Val_1, int Int_Par_Val_2) {

	int Int_Index,
		Int_Loc;

	Int_Loc = Int_Par_Val_1 + 5;
	Array_Par_1_Ref[Int_Loc] = Int_Par_Val_2;
	Array_Par_1_Ref[Int_Loc + 1] = Array_Par_1_Ref[Int_Loc];
	Array_Par_1_Ref[Int_Loc + 30] = Int_Loc;
	for (Int_Index = Int_Loc; Int_Index <= Int_Loc + 1; ++Int_Index) {
	    Array_Par_2_Ref[Int_Loc][Int_Index] = Int_Loc;
	}
	Array_Par_2_Ref[Int_Loc][Int_Loc - 1] += 1;
	Array_Par_2_Ref[Int_Loc + 20][Int_Loc] = Array_Par_1_Ref[Int_Loc];
	Int_Glob = 5;

    }

    int Func_1(char Char_Par_1_Val, char Char_Par_2_Val) {

	char Char_Loc_1,
		Char_Loc_2;

	Char_Loc_1 = Char_Par_1_Val;
	Char_Loc_2 = Char_Loc_1;
	if (Char_Loc_2 != Char_Par_2_Val) {
	    return Ident_1;
	} else {
	    return Ident_2;
	}

    }

    boolean Func_2(String String_Par_1_Ref, String String_Par_2_Ref) {

	int Int_Loc;
	char Char_Loc = '\0';

	Int_Loc = 2;
	while (Int_Loc <= 2) {
	    if (Func_1(String_Par_1_Ref.charAt(Int_Loc), String_Par_2_Ref.charAt(Int_Loc + 1)) == Ident_1) {
		Char_Loc = 'A';
		Int_Loc += 1;
	    }
	}
	if (Char_Loc >= 'W' && Char_Loc < 'Z') {
	    Int_Loc = 7;
	}
	if (Char_Loc == 'X') {
	    return true;
	} else {
	    if (String_Par_1_Ref.compareTo(String_Par_2_Ref) > 0) {
		Int_Loc += 7;
		return true;
	    } else {
		return false;
	    }
	}

    }

    boolean Func_3(int Enum_Par_Val) {

	int Enum_Loc;

	Enum_Loc = Enum_Par_Val;
	if (Enum_Loc == Ident_3) {
	    return true;
	} else {
	    return false;
	}

    }

    public static void main(String argv[]) {
	long nn = 100000000;
	int nl = 5;
	boolean noAsk = false;
	if (argv.length > 1) {
	    nn = Long.parseLong(argv[0]);
	    nl = Integer.parseInt(argv[1]);
	    noAsk = true;
	} else if (argv.length > 0) {
	    nn = Long.parseLong(argv[0]);
	    nl = 1;
	    noAsk = true;
	}
	Msg.out = System.err;

	Dhry dh = new Dhry();

	Msg.out.println("Dhrystone Benchmark, Version 2.1 (Language: Java)");
	Msg.out.println();
	Msg.out.println("java com.femtioprocent.fpd2.dhry.Dhry 100000000 5");
	Msg.out.println();
	Msg.out.print("Please give the number of runs through the benchmark ("
		+ f(dh.Number_Of_Runs)
		+ "): ");
	Msg.out.flush();
	try {
	    BufferedReader rdr
		    = new BufferedReader(new InputStreamReader(System.in));
	    String li;
	    if (noAsk) {
		li = null;
	    } else {
		li = rdr.readLine();
	    }
	    if (li == null || li.length() == 0) {
		dh.Number_Of_Runs = nn;
	    } else {
		dh.Number_Of_Runs = Long.valueOf(li).longValue();
	    }

	    Msg.out.print("Please give the number of loops through the benchmark ("
		    + f(nl)
		    + "): ");
	    Msg.out.flush();
	    if (noAsk) {
		li = null;
	    } else {
		li = rdr.readLine();
	    }
	    if (li == null || li.length() == 0)
		; else {
		nl = Integer.valueOf(li).intValue();
	    }

	    if (!noAsk) {
		long old_nn = dh.execute(100000);
		for (int i = 0; i < 100; i++) {
		    dh.execute();
		}
		dh.Number_Of_Runs = old_nn;
	    }

	    for (int n = 0; n < nl; n++) {
		Msg.out.println("Execution starts, " + dh.Number_Of_Runs + " runs through Dhrystone");
		String result = dh.execute();
		Msg.out.println(result);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    return;
	}
    }

    /**
     * java.lang.Runnable stuff
     */
    public void run() {
	execute();
	if (exitObserver != null) {
	    exitObserver.exitNotify();
	}
    }

    public void setExitObserver(ExitObserver eo) {
	exitObserver = eo;
    }

}
