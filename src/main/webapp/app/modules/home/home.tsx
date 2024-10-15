import './home.scss';
import React from 'react';
import { Link } from 'react-router-dom';
import { Row, Col, Alert, Card, CardBody, CardTitle } from 'reactstrap';
import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  // Mock data for statistics (replace with actual data from your backend)
  const stats = {
    quotationsReceived: 1250,
    farmersInsured: 850,
    totalFarmers: 2000,
    insuredPercentage: 42.5,
  };

  return (
    <Row>
      <Col md="12">
        <h1 className="display-4">Welcome to Policy Administration</h1>
        <p className="lead">Protecting farmers and their livelihoods</p>

        {account?.login ? (
          <Alert color="success">You are logged in as user &quot;{account.login}&quot;.</Alert>
        ) : (
          <Alert color="info">
            <Link to="/login" className="alert-link">
              Sign in
            </Link>{' '}
            or{' '}
            <Link to="/account/register" className="alert-link">
              Register
            </Link>{' '}
            to manage policies.
          </Alert>
        )}

        <Row className="mt-4">
          <Col md="3">
            <Card className="text-center mb-3">
              <CardBody>
                <CardTitle tag="h5">Quotations Received</CardTitle>
                <p className="display-4">{stats.quotationsReceived}</p>
              </CardBody>
            </Card>
          </Col>
          <Col md="3">
            <Card className="text-center mb-3">
              <CardBody>
                <CardTitle tag="h5">Farmers Insured</CardTitle>
                <p className="display-4">{stats.farmersInsured}</p>
              </CardBody>
            </Card>
          </Col>
          <Col md="3">
            <Card className="text-center mb-3">
              <CardBody>
                <CardTitle tag="h5">Total Farmers</CardTitle>
                <p className="display-4">{stats.totalFarmers}</p>
              </CardBody>
            </Card>
          </Col>
          <Col md="3">
            <Card className="text-center mb-3">
              <CardBody>
                <CardTitle tag="h5">Insured Percentage</CardTitle>
                <p className="display-4">{stats.insuredPercentage}%</p>
              </CardBody>
            </Card>
          </Col>
        </Row>

        <h2 className="mt-4">Our Agricultural Insurance Products</h2>
        <ul>
          <li>Crop Insurance</li>
          <li>Soil Moisture Insurance</li>
          <li>Farm Equipment Insurance</li>
          <li>Weather Index Insurance</li>
          <li>Multi-Peril Crop Insurance</li>
        </ul>

        <p>Protect your farm and secure your future. Contact us today to learn more about our tailored insurance solutions for farmers.</p>
      </Col>
    </Row>
  );
};

export default Home;
