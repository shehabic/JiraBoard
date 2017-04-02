package com.fullmob.jiraboard.ui.home;

import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.managers.user.UserManager;
import com.fullmob.jiraboard.ui.AbstractPresenter;

/**
 * Created by shehabic on 02/04/2017.
 */
public class HomeScreenPresenter extends AbstractPresenter<HomeScreenView> {

    private final EncryptedStorage encryptedStorage;
    private final DBManagerInterface db;
    private final UserManager userManager;

    public HomeScreenPresenter(
        HomeScreenView view,
        EncryptedStorage encryptedStorage,
        DBManagerInterface db,
        UserManager userManager
    ) {
        super(view);
        this.encryptedStorage = encryptedStorage;
        this.db = db;
        this.userManager = userManager;
    }

    public void onViewResumed() {
        getView().renderUserDetails(userManager.getUser());
        getView().setTitle(db.getProject(encryptedStorage.getDefaultProject()).getName());
    }
}
