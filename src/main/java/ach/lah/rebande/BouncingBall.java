package ach.lah.rebande;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.log4j.Log4j;

@Log4j
public class BouncingBall extends Application {

	private static final int R = 20;

	double M = 1;

	final Pane canvas = new Pane();
	Scene scene = new Scene(canvas, 300, 300, Color.ALICEBLUE);

	public double[] scal(double A[][], double B[]) {
		double C[] = new double[4];
		// multiplication
		log.debug("A : " + A);
		log.debug("B : " + B);

		for (int i = 0; i < 4; i++) {
			C[i] = 0;
			for (int k = 0; k < 4; k++) {
				C[i] += A[i][k] * B[k];
			}
		}
		log.debug("C : " + C);
		return C;
	}

	private void collision(MyCircle d1, MyCircle d2) {

		double nx = d2.getCenterX() - d1.getCenterX();
		double ny = d2.getCenterY() - d1.getCenterY();
		double tx = ny;
		double ty = -nx;
		double[][] A = { //
				{ M, 0, M, 0 }, //
				{ 0, M, 0, M }, //
				{ tx, ty, 0, 0 }, //
				{ nx, ny, -nx, -ny } //
		};

		double[] B = { //
				M * d1.getDx() + M * d2.getDx(), //
				M * d1.getDy() + M * d2.getDy(), //
				d1.getDx() * tx + d1.getDy() * ty, //
				-((d1.getDx() - d2.getDx()) * nx + (d1.getDy() - d2.getDy()) * ny) //
		};
		double[] s = scal(A, B);
		d1.setLayoutX(d1.getLayoutX() + s[0]);
		d1.setLayoutY(d1.getLayoutY() + s[1]);
		d2.setLayoutX(d2.getLayoutX() + s[2]);
		d2.setLayoutY(d2.getLayoutY() + s[3]);
	}

	private static List<MyCircle> balls = new ArrayList<MyCircle>();

	@Override
	public void start(Stage stage) {

		addMyCircle(stage, 5, 25, 2, 3);
		addMyCircle(stage, 50, 100, 3, 2);
	}

	private void addMyCircle(Stage stage, int x, int y, int dx, int dy) {
		final MyCircle ball = new MyCircle(R, Color.CADETBLUE, dx, dy);
		balls.add(ball);
		ball.relocate(x, y);

		canvas.getChildren().add(ball);

		stage.setTitle("Animated Ball");
		stage.setScene(scene);
		stage.show();

		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				// move the ball
				ball.setLayoutX(ball.getLayoutX() + ball.getDx());
				ball.setLayoutY(ball.getLayoutY() + ball.getDy());

				Bounds bounds = canvas.getBoundsInLocal();

				// If the ball reaches the left or right border make the step negative
				if (ball.getLayoutX() <= (bounds.getMinX() + ball.getRadius()) || ball.getLayoutX() >= (bounds.getMaxX() - ball.getRadius())) {

					ball.setDx(-ball.getDx());

				}

				// If the ball reaches the bottom or top border make the step negative
				if ((ball.getLayoutY() >= (bounds.getMaxY() - ball.getRadius())) || (ball.getLayoutY() <= (bounds.getMinY() + ball.getRadius()))) {

					ball.setDy(-ball.getDy());

				}

				for (final MyCircle circle : balls) {
					if (!circle.equals(ball)) {
						Shape intersect = Shape.intersect(circle, ball);
						if (intersect.getBoundsInLocal().getWidth() != -1) {
							collision(circle, ball);
							if (Color.GREEN.equals(circle.getFill())) {
								circle.setFill(Color.BLUE);
							} else {
								circle.setFill(Color.GREEN);
							}
						}
					}
				}
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	public static void main(String[] args) {
		launch();
	}
}