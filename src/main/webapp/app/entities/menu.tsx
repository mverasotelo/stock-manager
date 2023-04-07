import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/article">
        <Translate contentKey="global.menu.entities.article" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/stock">
        <Translate contentKey="global.menu.entities.stock" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/store">
        <Translate contentKey="global.menu.entities.store" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/action">
        <Translate contentKey="global.menu.entities.action" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/alert">
        <Translate contentKey="global.menu.entities.alert" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/employee">
        <Translate contentKey="global.menu.entities.employee" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
