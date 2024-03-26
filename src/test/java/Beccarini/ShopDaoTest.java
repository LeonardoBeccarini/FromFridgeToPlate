package Beccarini;

import com.example.fromfridgetoplate.logic.dao.ShopDAO;
import com.example.fromfridgetoplate.logic.dao.SingletonConnector;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.model.Shop;
import org.junit.AfterClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ShopDaoTest {
    @Test
    public void saveNewShop(){
        Shop shop = new Shop(
                "pluto@gmail.com", "pippo12", "CheNeSo",
                "Via paperino 44, RM", "12345678912", "3976542336");
        ShopDAO shopDAO = new ShopDAO();
        try {
            assertTrue( shopDAO.saveShop(shop));
        } catch (DbException e) {
            //don't care handling exceptions
        }
    }

    @Test
    public void saveAlreadyPresentShop(){
        Shop shop = new Shop(
                "quircio@gmail.com", "quircioreseller", "Pippi",
                "Via pippo 23, RM", "12345678945", "3456798334");
        ShopDAO shopDAO = new ShopDAO();
        try {
            assertFalse( shopDAO.saveShop(shop));
        } catch (DbException e) {
            //don't care handling exceptions
        }
    }
    @Test
    public void getShopByEmail(){
        Shop shop = new Shop(
                "quircio@gmail.com", "Pippi", "Via pippo 23, RM",
                "12345678945", "3456798334");
        ShopDAO shopDAO = new ShopDAO();
        Shop ouptutShop = null;
        try {
            ouptutShop = shopDAO.retrieveShopByEmail("quircio@gmail.com");
        } catch (DbException e) {
            //don't care handling exceptions
        }
        assertEquals(shop.getEmail(), Objects.requireNonNull(ouptutShop).getEmail());
    }


    @AfterClass
    public static void ShopTableCleanUp(){
        Connection connection = SingletonConnector.getInstance().getConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM SHOP WHERE Email = 'pluto@gmail.com'");
            PreparedStatement preparedStatement2 = connection.prepareStatement("DELETE FROM USER WHERE Email = 'pluto@gmail.com'");
            preparedStatement2.execute();
            preparedStatement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
