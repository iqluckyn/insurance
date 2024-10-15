import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BusinessType from './business-type';
import BusinessTypeDetail from './business-type-detail';
import BusinessTypeUpdate from './business-type-update';
import BusinessTypeDeleteDialog from './business-type-delete-dialog';

const BusinessTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BusinessType />} />
    <Route path="new" element={<BusinessTypeUpdate />} />
    <Route path=":id">
      <Route index element={<BusinessTypeDetail />} />
      <Route path="edit" element={<BusinessTypeUpdate />} />
      <Route path="delete" element={<BusinessTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BusinessTypeRoutes;
