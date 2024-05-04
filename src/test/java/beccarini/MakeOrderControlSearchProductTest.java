package beccarini;

import com.example.fromfridgetoplate.logic.bean.FoodItemBean;
import com.example.fromfridgetoplate.logic.bean.FoodItemListBean;
import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.bean.UserBean;
import com.example.fromfridgetoplate.logic.control.LoginController;
import com.example.fromfridgetoplate.logic.control.MakeOrderControl;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.EmptyCatalogException;
import com.example.fromfridgetoplate.logic.exceptions.NotExistentUserException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MakeOrderControlSearchProductTest {
    @Test
    public void searchProduct(){
        //devo fare prima il login
        LoginController loginController = new LoginController();
        try {
            loginController.login(new UserBean("leonardo.beccarini@hotmail.com", "pippo23"));
        } catch (NotExistentUserException | DAOException e) {
            //don't care
        }
        //shop da cui prendo il catalogo
        ShopBean selectedShopBean = new ShopBean("Pippi", "Via pippo 23, RM", "3456798334", "12345678945");
        //input che inserisce l'utente
        FoodItemBean searchInfo = new FoodItemBean("lattuga");
        try {
            MakeOrderControl makeOrderControl = new MakeOrderControl(selectedShopBean);
            FoodItemListBean filteredItemList = makeOrderControl.searchProduct(searchInfo);
            assertEquals("lattuga", filteredItemList.getList().get(0).getName());
            assertEquals(8.99, filteredItemList.getList().get(0).getPrice(), 0.001);
        } catch (IOException | DAOException |EmptyCatalogException e) {
            //don't care
        }
    }
}
