package assets;

import org.jbox2d.common.Vec2;

public class SinCosTable{
	Vec2[] a;
	public int na;
	public SinCosTable(int number_of_angles){
		na = number_of_angles;
		a = new Vec2[na];
		a[0] = new Vec2(1,0);
		for(int i = 1;i<na-1;i++){
			a[i] = new Vec2((float)Math.cos(i*(2*Math.PI)/na), (float)Math.sin(i*(2*Math.PI)/na));
		}
	}
	public Vec2 calculate(int number){
		number = number%(na-1);
		return a[number];
	}
}//End sinCosinTable
