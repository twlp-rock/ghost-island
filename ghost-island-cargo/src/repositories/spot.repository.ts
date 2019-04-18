import {DefaultCrudRepository} from '@loopback/repository';
import {Spot} from '../models';
import {MongoDataSource} from '../datasources';
import {inject} from '@loopback/core';

export class SpotRepository extends DefaultCrudRepository<
  Spot,
  typeof Spot.prototype.id
> {
  constructor(
    @inject('datasources.Mongo') dataSource: MongoDataSource,
  ) {
    super(Spot, dataSource);
  }
}
