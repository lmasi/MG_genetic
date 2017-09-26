package mg.machine;

import java.util.ArrayList;
import java.util.HashMap;

import mg.gui.MyPoint;

public class Section
{
	private int index = -1;
	private int groupIndex;

	private ArrayList<Machine> l_machines = new ArrayList<Machine>();
	private ArrayList<Machine> r_machines = new ArrayList<Machine>();
	private HashMap<String, Double> leftMachinePoint = new HashMap<String, Double>();
	private HashMap<String, Double> rightMachinePoint = new HashMap<String, Double>();

	private static final double WIDTH = 40;
	private double depth = 0;
	private double WIDTH_BAY = 2;

	private boolean l_full = false;
	private boolean r_full = false;

	private double l_before_machine = 0;
	private double l_start_point = 0;
	private double r_before_machine = 0;
	private double r_start_point = 0;


	public Section(final int index, final int groupIndex)
	{
		this.index = index;	
		this.groupIndex = groupIndex;
	}

	public double getDepth() { return this.depth; }
	public int getLSize() { return this.l_machines.size(); }
	public int getRSize() { return this.r_machines.size(); }

	public boolean addMachine(Machine machine) throws Exception
	{
		if(machine.get_index() != this.groupIndex) throw new Exception();

		if(!l_full)
		{
		System.out.println("insert " + machine + "to left");
			if(l_start_point - l_before_machine >= machine.get_l_margin())
			{
				double tmp_start_point = l_start_point;

				l_before_machine = l_start_point + machine.get_width();
				l_start_point = l_before_machine + machine.get_r_margin();

				if(l_start_point > WIDTH) 
				{
					l_full = true;
					addMachine(machine);
				}
				
				if(!l_full){
					leftMachinePoint.put(machine.get_name(), tmp_start_point);
					l_machines.add(machine);
				}

			}	
			
			else
			{
				double tmp_before_machine = l_before_machine;
				double tmp_start_point = l_start_point;

				
				l_before_machine = l_start_point + machine.get_l_margin() + machine.get_width();
				l_start_point = l_before_machine + machine.get_r_margin();

				if(l_start_point > WIDTH) 
				{
					l_full = true;
					addMachine(machine);
				}
				
				if(!l_full){
					leftMachinePoint.put(machine.get_name(), tmp_before_machine + machine.get_l_margin());
					l_machines.add(machine);
				}
			}
		}

		else if(!r_full)
		{
		System.out.println("insert " + machine + "to right");
			if(r_start_point - r_before_machine >= machine.get_r_margin())
			{
				double tmp_start_point = r_start_point;

				r_before_machine = r_start_point + machine.get_width();
				r_start_point = r_before_machine + machine.get_l_margin();

				if(r_start_point > WIDTH) 
				{
					r_full = true;
					return false;
				}

				rightMachinePoint.put(machine.get_name(), tmp_start_point);
				r_machines.add(machine);
			}	
			
			else
			{
				double tmp_before_machine = r_before_machine;
				double tmp_start_point = r_start_point;

				
				r_before_machine = r_start_point + machine.get_r_margin() + machine.get_width();
				r_start_point = r_before_machine + machine.get_l_margin();
				
				if(r_start_point > WIDTH)
				{
					r_full = true;
					return false;
				}

				rightMachinePoint.put(machine.get_name(), tmp_before_machine + machine.get_r_margin());
				r_machines.add(machine);
			}
		}
		else
			return false;

		return true;
	}
	
	public Section build()
	{
		double l_max = l_machines.get(0).get_b_margin() + l_machines.get(0).get_depth();
		double r_max = r_machines.get(0).get_b_margin();

		for(Machine m : l_machines)
			if(l_max < m.get_b_margin() + m.get_depth())
				l_max = m.get_b_margin() + m.get_depth();

		for(Machine m : r_machines)
			if(r_max < m.get_b_margin() + m.get_depth())
				r_max = m.get_b_margin() + m.get_depth();

		this.depth = l_max + WIDTH_BAY + r_max;
		
		return this;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("left");
		sb.append("\n{");
			for(Machine m : l_machines)
				sb.append("\n\t" + m + " : " + leftMachinePoint.get(m.get_name()) + "~" + (leftMachinePoint.get(m.get_name()) + m.get_width() + m.get_r_margin()));
		sb.append("\n}");
		sb.append("\n");
		sb.append("\nright");
		sb.append("\n{");
			for(Machine m : r_machines)
				sb.append("\n\t" + m + " : " + rightMachinePoint.get(m.get_name()) + "~" + (rightMachinePoint.get(m.get_name()) + m.get_width() + m.get_l_margin()));
		sb.append("\n}");

		return sb.toString();
	}

	private class PivotPoint extends MyPoint
	{
		public PivotPoint(double x, double y) { super(x,y); }
		public PivotPoint(int x, int y) { super(x, y); }
	}

}