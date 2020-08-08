package com.ardobot.ardocontrol.menu.presenter;

import com.ardobot.ardocontrol.menu.interactors.MenuActivityInteractors;
import com.ardobot.ardocontrol.menu.interactors.MenuActivityInteractorsImpl;
import com.ardobot.ardocontrol.menu.view.MenuActivityView;

public class MenuActivityPresenterImpl implements MenuActivityPresenter {
    private MenuActivityView menuActivityView;
    private MenuActivityInteractors menuActivityInteractors;

    public MenuActivityPresenterImpl(MenuActivityView menuActivityView) {
        this.menuActivityView = menuActivityView;
        menuActivityInteractors = new MenuActivityInteractorsImpl(this);
    }

    @Override
    public void ScanViewId(boolean typeScan) {
        menuActivityView.ScanViewId(typeScan);
    }

    @Override
    public void Loggout() {
        menuActivityInteractors.logout();
        menuActivityView.GoActivityLogin();
    }
}
