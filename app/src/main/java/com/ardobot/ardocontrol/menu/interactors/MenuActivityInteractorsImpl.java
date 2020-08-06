package com.ardobot.ardocontrol.menu.interactors;

import com.ardobot.ardocontrol.menu.presenter.MenuActivityPresenter;
import com.google.firebase.auth.FirebaseAuth;

public class MenuActivityInteractorsImpl implements MenuActivityInteractors {
    private MenuActivityPresenter menuActivityPresenter;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public MenuActivityInteractorsImpl(MenuActivityPresenter menuActivityPresenter) {
        this.menuActivityPresenter = menuActivityPresenter;
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
    }
}
