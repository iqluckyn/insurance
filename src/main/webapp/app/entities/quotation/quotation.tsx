import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './quotation.reducer';

const Quotation = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const quotationList = useAppSelector(state => state.quotation.entities);
  const loading = useAppSelector(state => state.quotation.loading);
  const totalItems = useAppSelector(state => state.quotation.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = fieldName => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="quotation-heading" data-cy="QuotationHeading">
        Quotations
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/quotation/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Quotation
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {quotationList && quotationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('startOfRiskPeriod')}>
                  Start Of Risk Period <FontAwesomeIcon icon={getSortIconByFieldName('startOfRiskPeriod')} />
                </th>
                <th className="hand" onClick={sort('lengthOfRiskPeriod')}>
                  Length Of Risk Period <FontAwesomeIcon icon={getSortIconByFieldName('lengthOfRiskPeriod')} />
                </th>
                <th className="hand" onClick={sort('depth')}>
                  Depth <FontAwesomeIcon icon={getSortIconByFieldName('depth')} />
                </th>
                <th className="hand" onClick={sort('claimsFrequency')}>
                  Claims Frequency <FontAwesomeIcon icon={getSortIconByFieldName('claimsFrequency')} />
                </th>
                <th className="hand" onClick={sort('insuredValue')}>
                  Insured Value <FontAwesomeIcon icon={getSortIconByFieldName('insuredValue')} />
                </th>
                <th className="hand" onClick={sort('bestPremium')}>
                  Best Premium <FontAwesomeIcon icon={getSortIconByFieldName('bestPremium')} />
                </th>
                <th className="hand" onClick={sort('insuredRate')}>
                  Insured Rate <FontAwesomeIcon icon={getSortIconByFieldName('insuredRate')} />
                </th>
                <th className="hand" onClick={sort('insuredPremium')}>
                  Insured Premium <FontAwesomeIcon icon={getSortIconByFieldName('insuredPremium')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  Created At <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  Updated At <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th>
                  Season <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Farmer <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Product <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Business <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Quotation Status <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {quotationList.map((quotation, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/quotation/${quotation.id}`} color="link" size="sm">
                      {quotation.id}
                    </Button>
                  </td>
                  <td>
                    {quotation.startOfRiskPeriod ? (
                      <TextFormat type="date" value={quotation.startOfRiskPeriod} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{quotation.lengthOfRiskPeriod}</td>
                  <td>{quotation.depth}</td>
                  <td>{quotation.claimsFrequency}</td>
                  <td>{quotation.insuredValue}</td>
                  <td>{quotation.bestPremium}</td>
                  <td>{quotation.insuredRate}</td>
                  <td>{quotation.insuredPremium}</td>
                  <td>{quotation.createdAt ? <TextFormat type="date" value={quotation.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{quotation.updatedAt ? <TextFormat type="date" value={quotation.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{quotation.season ? <Link to={`/season/${quotation.season.id}`}>{quotation.season.seasonName}</Link> : ''}</td>
                  <td>{quotation.farmer ? <Link to={`/farmer/${quotation.farmer.id}`}>{quotation.farmer.firstname}</Link> : ''}</td>
                  <td>{quotation.product ? <Link to={`/product/${quotation.product.id}`}>{quotation.product.id}</Link> : ''}</td>
                  <td>{quotation.business ? <Link to={`/business/${quotation.business.id}`}>{quotation.business.id}</Link> : ''}</td>
                  <td>
                    {quotation.quotationStatus ? (
                      <Link to={`/quotation-status/${quotation.quotationStatus.id}`}>{quotation.quotationStatus.statusCode}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/quotation/${quotation.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/quotation/${quotation.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Quotations found</div>
        )}
      </div>
      {totalItems ? (
        <div className={quotationList && quotationList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Quotation;
