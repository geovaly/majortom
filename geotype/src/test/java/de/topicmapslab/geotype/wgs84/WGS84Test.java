package de.topicmapslab.geotype.wgs84;

import junit.framework.TestCase;
import de.topicmapslab.geotype.wgs84.Wgs84Coordinate.Orientation;

public class WGS84Test extends TestCase {

	public void testname() {

		// System.out.println(regExp.matcher("12�15'20'' N").matches());
		// System.out.println(regExp.matcher("12�15'20'' E").matches());
		// System.out.println(regExp.matcher("12�15'20'' W").matches());
		// System.out.println(regExp.matcher("12�15'20'' S").matches());
		// System.out.println(regExp.matcher("12�15'20''").matches());
		// System.out.println(regExp.matcher("12�15'20,00''").matches());
		// System.out.println(regExp.matcher("12�15'").matches());

	}

	public void testFromDegree() {
		System.out.println(new Wgs84Coordinate(16.324525, Orientation.N).toString());
	}

}
