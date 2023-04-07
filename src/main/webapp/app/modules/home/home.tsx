import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert, Button } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import Login from '../login/login';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowRight, faBox} from '@fortawesome/free-solid-svg-icons';
import { ActionType } from 'app/shared/model/enumerations/action-type.model';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="12">
        {account?.login ? (
          <div>
            <div>
              {/* <Alert color="success">
                <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                  You are logged in as user {account.login}.
                </Translate>
              </Alert> */}
            </div>
            <div className="d-flex flex-wrap">
              <Button className="flex-fill col-6 m-4 blue-bg p-3" tag={Link} to={`/action/new/${ActionType.IN}`} color="info" size="lg">
                <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="home.buttons.in">Ingreso</Translate>
                </span>
              </Button>
              <Button className="flex-fill col-6 m-4 blue-bg p-3" tag={Link} to={`/action/new/${ActionType.OUT}`} color="info" size="lg">
                <FontAwesomeIcon icon={faArrowRight}/>{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="home.buttons.out">Egreso</Translate>
                </span>
              </Button>
              <Button className="flex-fill col-6 m-4 blue-bg p-3" tag={Link} to={`/stock`} color="info" size="lg">
                <FontAwesomeIcon icon="list" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="home.buttons.inventory">Inventario</Translate>
                </span>
              </Button>
              <Button className="flex-fill col-6 m-4 blue-bg p-3" tag={Link} to={`/alert`} color="info" size="lg">
                <FontAwesomeIcon icon="bell" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="home.buttons.alerts">Alertas</Translate>
                </span>
              </Button>
              <Button className="flex-fill col-6 m-4 blue-bg p-3" tag={Link} to={`/article`} color="info" size="lg">
                <FontAwesomeIcon icon={faBox}/>{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="home.buttons.articles">Artículos por código</Translate>
                </span>
              </Button>
            </div>
          </div>

        ) : (
          <>
            <h2>
              <Translate contentKey="global.menu.account.login">Welcome!</Translate>
            </h2>
            <br />
            <Login />
          </>
        )}
      </Col>
    </Row>
  );
};

export default Home;
