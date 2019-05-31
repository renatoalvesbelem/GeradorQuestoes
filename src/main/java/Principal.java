import br.com.view.CadastrarQuestao;
import br.com.view.CadastroMateria;
import br.com.view.ConsultarQuestao;
import br.com.view.CadastroSerie;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Principal extends Application {
    private static Pane root;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        MenuBar barraMenu = adicionarBarraMenu(0, 0);

        Menu menuAdicionar = new Menu("Cadastro");
        MenuItem cadastrarMateria = new MenuItem("Materia");
        cadastrarMateria.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                novaScena();
            }
        });
        MenuItem cadastrarQuestao = new MenuItem("Questões");
        cadastrarQuestao.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                questaoScena();
            }
        });
        MenuItem cadastrarSerie = new MenuItem("Série");
        cadastrarSerie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                scenaSerie();
            }
        });

        menuAdicionar.getItems().add(cadastrarMateria);
        menuAdicionar.getItems().add(cadastrarQuestao);
        menuAdicionar.getItems().add(cadastrarSerie);


        adicionarMenu(barraMenu, menuAdicionar);


        root = new Pane();
        root.getChildren().add(barraMenu);

        Scene scene = new Scene(root);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Gerenciador Questões");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    private void questaoScena() {
        new CadastrarQuestao().start(new Stage());
    }

    private void scenaSerie() {
        new CadastroSerie().start(new Stage());
    }

    public static MenuBar adicionarBarraMenu(int x, int y) {
        MenuBar menuBar = new MenuBar();
        menuBar.setLayoutX(x);
        menuBar.setLayoutY(y);
        return menuBar;
    }

    public static void adicionarMenu(MenuBar barraMenu, Menu... menus) {
        barraMenu.getMenus().addAll(menus);
    }

    public static void novaScena() {
        new CadastroMateria().start(new Stage());
    }
}
