import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Business from './business';
import BusinessDetail from './business-detail';
import BusinessUpdate from './business-update';
import BusinessDeleteDialog from './business-delete-dialog';

const BusinessRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Business />} />
    <Route path="new" element={<BusinessUpdate />} />
    <Route path=":id">
      <Route index element={<BusinessDetail />} />
      <Route path="edit" element={<BusinessUpdate />} />
      <Route path="delete" element={<BusinessDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BusinessRoutes;
