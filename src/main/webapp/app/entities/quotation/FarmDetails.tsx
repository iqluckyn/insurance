// FarmDetails.tsx
import React, { useState, useEffect } from 'react';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';

const FarmDetails = ({ hoveredCell, selectedFarm, onFarmSelect, isNewQuotation }) => {
  const [farm, setFarm] = useState({ latitude: '', longitude: '', cellIdentifier: '' });

  useEffect(() => {
    if (selectedFarm) {
      setFarm(selectedFarm);
    } else if (isNewQuotation && hoveredCell) {
      setFarm({
        latitude: hoveredCell.lat,
        longitude: hoveredCell.lng,
        cellIdentifier: hoveredCell.cellIdentifier,
      });
    }
  }, [selectedFarm, hoveredCell, isNewQuotation]);

  const handleSubmit = e => {
    e.preventDefault();
    onFarmSelect(farm);
  };

  return (
    <div>
      <h3>Farm Details</h3>
      <Form onSubmit={handleSubmit}>
        <FormGroup>
          <Label for="latitude">Latitude</Label>
          <Input
            type="number"
            name="latitude"
            id="latitude"
            value={farm.latitude}
            onChange={e => setFarm({ ...farm, latitude: e.target.value })}
            readOnly={!isNewQuotation}
          />
        </FormGroup>
        <FormGroup>
          <Label for="longitude">Longitude</Label>
          <Input
            type="number"
            name="longitude"
            id="longitude"
            value={farm.longitude}
            onChange={e => setFarm({ ...farm, longitude: e.target.value })}
            readOnly={!isNewQuotation}
          />
        </FormGroup>
        <FormGroup>
          <Label for="cellIdentifier">Cell Identifier</Label>
          <Input
            type="text"
            name="cellIdentifier"
            id="cellIdentifier"
            value={farm.cellIdentifier}
            onChange={e => setFarm({ ...farm, cellIdentifier: e.target.value })}
            readOnly={!isNewQuotation}
          />
        </FormGroup>
        {isNewQuotation && (
          <Button color="primary" type="submit">
            Save Farm
          </Button>
        )}
      </Form>
    </div>
  );
};

export default FarmDetails;
