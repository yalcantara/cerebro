package com.yaison.cerebro.graph;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;




import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.ChartScene;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapGrayscale;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.ScatterMultiColor;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import com.yaison.cerebro.math.BiVarFunc;
import com.yaison.cerebro.math.Matrix;
import com.yaison.cerebro.math.Vector;
public class Charts {

	

	private static JFrame frame(JPanel c) {
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();

		JFrame f = new JFrame();
		f.setContentPane(c);
		f.setPreferredSize(new Dimension(800, 500));
		f.setSize(f.getPreferredSize());
		f.setVisible(true);

		int posx = (int) ((d.getWidth() - f.getWidth()) / 2);
		int posy = (int) ((d.getHeight() - f.getHeight()) / 2);
		if (posy < d.getHeight() * 0.2) {
			posy = (int) (d.getHeight() * 0.2);
		}
		f.setLocation(posx, posy);

		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		return f;
	}

	public static Chart line(Matrix m, Color color, int xcol, int ycol, int zcol) {

		// Create a chart and add scatter
		AWTChartComponentFactory fac = new AWTChartComponentFactory();

		Chart chart = new Chart(fac, Quality.Nicest);
		chart.getAxeLayout().setMainColor(Color.WHITE);
		chart.getView().setBackgroundColor(Color.BLACK);

		line(chart, m, color, xcol, ycol, zcol);
		ChartLauncher.openChart(chart);

		return chart;
	}

	public static Chart line(Chart chart, Matrix m, Color color, int xcol,
			int ycol, int zcol) {

		int size = m.rows();

		List<LineStrip> lines = new ArrayList<LineStrip>();

		// Create scatter points
		for (int i = 0; i < size; i++) {
			LineStrip line = new LineStrip();

			Point p1 = point(m, color, i, xcol, ycol, zcol);
			if (i + 1 < size) {
				Point p2 = point(m, color, i + 1, xcol, ycol, zcol);
				line.add(p1);
				line.add(p2);
				line.setDisplayed(true);
				lines.add(line);
			}
		}

		// Create a drawable scatter with a colormap

		for (LineStrip line : lines) {
			chart.getScene().add(line);
		}

		return chart;
	}

	private static Point point(Matrix m, Color color, int i, int xcol,
			int ycol, int zcol) {
		double x = m.get(i, xcol);
		double y = 0.0;
		if (ycol >= 0) {
			y = m.get(i, ycol);
		}
		double z = m.get(i, zcol);
		return new Point(new Coord3d(x, y, z), color);
	}
	
	public static void vector(Chart c, Matrix v){
		for(int i =0; i < v.n(); i++){
			vector(c, v.getColumn(i));
		}
	}
	
	public static void vector(Chart c, Vector v){
		
		Point a = new Point(new Coord3d(0, 0, 0));
		Point b = new Point(new Coord3d(v.get(0), v.get(1), v.get(2)));
		
		LineStrip l = new LineStrip(a, b);
		l.setWidth(2);
		
		b.setWidth(7);
		ChartScene scene = c.getScene();
		scene.add(l);
		scene.add(b);
	}

	public static Chart scatter(Matrix m) {
		return scatter(m, 0, 1, 2);
	}
	
	public static Chart scatter(Matrix m, int xcol, int ycol, int zcol) {
		// Create a chart and add scatter
		AWTChartComponentFactory fac = new AWTChartComponentFactory();

		Chart chart = new Chart(fac, Quality.Nicest);
		chart.getAxeLayout().setMainColor(Color.WHITE);
		chart.getView().setBackgroundColor(Color.BLACK);

		scatter(chart, m, xcol, ycol, zcol);
		ChartLauncher.openChart(chart);
		return chart;
	}

	public static Chart scatter(Chart chart, Matrix m, int xcol, int ycol,
			int zcol) {

		int size = m.rows();
		Coord3d[] points = new Coord3d[size];

		// Create scatter points
		for (int i = 0; i < size; i++) {
			double x = m.get(i, xcol);
			double y = 0.0;
			if (ycol >= 0) {
				y = m.get(i, ycol);
			}
			double z = m.get(i, zcol);
			points[i] = new Coord3d(x, y, z);
		}

		double min = m.colMin(zcol);
		double max = m.colMax(zcol);

		// Create a drawable scatter with a colormap
		ScatterMultiColor scatter = new ScatterMultiColor(points, null,
				new ColorMapper(new ColorMapRainbow(), min, max), 8.0f);

		chart.getScene().add(scatter);

		return chart;
	}
	
	

	public static Chart surface(BiVarFunc func, double min, double max) {
		Range range = new Range(min, max);
		return surface(func, range, range);
	}

	public static Chart surface(Chart chart, BiVarFunc func) {
		return surface(chart, func, null, null);
	}

	public static Chart surface(Chart chart, BiVarFunc func, double min,
			double max) {
		// Define range and precision for the function to plot
		Range range = new Range(min, max);

		return surface(chart, func, range, range);

	}

	public static Chart surface(BiVarFunc func, double xmin, double xmax,
			double ymin, double ymax) {
		return surface(func, new Range(xmin, xmax), new Range(ymin, ymax));
	}

	public static Chart surface(BiVarFunc func, Range rangex, Range rangey) {
		AWTChartComponentFactory fac = new AWTChartComponentFactory();

		Chart chart = new Chart(fac, Quality.Nicest);
		chart.getAxeLayout().setMainColor(Color.WHITE);
		chart.getView().setBackgroundColor(Color.BLACK);

		surface(chart, func, rangex, rangey);
		ChartLauncher.openChart(chart);
		return chart;
	}

	public static Chart surface(Chart chart, BiVarFunc func, Range rangex,
			Range rangey) {

		// Define a function to plot
		Mapper mapper = new Mapper() {
			public double f(double x, double y) {
				return func.apply(x, y);
			}
		};

		int steps = 50;

		// Create a surface drawing that function
		if (rangex == null) {
			BoundingBox3d box = chart.getView().getBounds();
			rangex = new Range(box.getXmin(), box.getXmax());
		}

		if (rangey == null) {
			BoundingBox3d box = chart.getView().getBounds();
			rangey = new Range(box.getYmin(), box.getYmax());
		}

		Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(rangex,
				steps, rangey, steps), mapper);

		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface
				.getBounds().getZmin(), surface.getBounds().getZmax(),
				new Color(1, 1, 1, .5f)));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(false);
		surface.setWireframeColor(Color.WHITE);

		// Create a chart and add the surface

		chart.getScene().getGraph().add(surface);

		return chart;
	}
}
