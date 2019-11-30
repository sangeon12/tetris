package net.gondr.tetris;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.gondr.domain.Player;
import net.gondr.domain.Player2;

public class App extends Application 
{
	public static App app;
	public Game game = null;
	public Game2 game2= null;
	public Player player= null;
	
	
    public static void main( String[] args )
    {
        launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		app = this;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
					getClass().getResource("/net/gondr/views/Main.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			
			Scene scene = new Scene(root);
			
			scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
				if(game != null) {
					game.keyHandler(e);
				}
			});
			scene.addEventFilter(KeyEvent.KEY_PRESSED, e2 -> {
				if(game2 != null) {
					game2.keyHandler(e2);
				}
			});
			
			
			
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}












