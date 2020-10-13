package ach.lah.rebande;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public class MyCircle extends Circle {

	public MyCircle(int r, Color cadetblue, double dx, double dy) {
		super(r, cadetblue);
		this.dx = dx;
		this.dy = dy;
	}

	private double dx = 10;// (Math.random() * (max - min)) + min;// Step on x or velocity
	private double dy = 10;// (Math.random() * (max - min)) + min;// Step on y

	public void setDx(double dx) {
		log.debug("dx : " + dx);
		this.dx = dx;
	}

	public void setDy(double dy) {
		log.debug("dy : " + dy);
		this.dy = dy;
	}

}
