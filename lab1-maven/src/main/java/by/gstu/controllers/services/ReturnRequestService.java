package by.gstu.controllers.services;

import by.gstu.models.dao.DAOFactory;
import by.gstu.models.dao.ReturnRequestDAO;
import by.gstu.models.utils.ParserJSON;
import org.json.JSONArray;

import java.util.ArrayList;

public class ReturnRequestService {

    private static ReturnRequestService instance;
    private ReturnRequestDAO returnRequestDAO;

    private ReturnRequestService() {
        returnRequestDAO = DAOFactory.getDAOFactory().getReturnRequestDAO();
    }

    public static ReturnRequestService getInstance() {
        if (instance == null) {
            instance = new ReturnRequestService();
        }
        return instance;
    }

    public JSONArray getAllReturnRequests(int clientId) {
        return null;
    }

    public JSONArray getAllReturnRequests() {
        return ParserJSON.toJSONArray(new ArrayList<>(returnRequestDAO.readAll()));
    }
}
