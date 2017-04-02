package com.fullmob.jiraboard.ui.home;

import com.fullmob.jiraapi.models.User;
import com.fullmob.jiraboard.ui.BaseView;

/**
 * Created by shehabic on 02/04/2017.
 */

public interface HomeScreenView extends BaseView {
    void setTitle(String name);

    void renderUserDetails(User user);
}
