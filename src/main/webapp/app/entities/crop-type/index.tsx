import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CropType from './crop-type';
import CropTypeDetail from './crop-type-detail';
import CropTypeUpdate from './crop-type-update';
import CropTypeDeleteDialog from './crop-type-delete-dialog';

const CropTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CropType />} />
    <Route path="new" element={<CropTypeUpdate />} />
    <Route path=":id">
      <Route index element={<CropTypeDetail />} />
      <Route path="edit" element={<CropTypeUpdate />} />
      <Route path="delete" element={<CropTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CropTypeRoutes;
