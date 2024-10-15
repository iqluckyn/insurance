import './footer.scss';

import React from 'react';
import { Col, Row } from 'reactstrap';

const Footer = () => (
  <div className="footer page-content">
    <Row>
      <Col md="12" className="text-center">
        <p className="mb-0">Developed by IQLogistica (Pty) Ltd &copy; {new Date().getFullYear()}</p>
      </Col>
    </Row>
  </div>
);

export default Footer;
