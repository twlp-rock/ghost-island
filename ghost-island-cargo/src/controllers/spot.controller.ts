import {
  Count,
  CountSchema,
  Filter,
  repository,
  Where,
} from '@loopback/repository';
import {
  post,
  param,
  get,
  getFilterSchemaFor,
  getWhereSchemaFor,
  patch,
  put,
  del,
  requestBody,
} from '@loopback/rest';
import {Spot} from '../models';
import {SpotRepository} from '../repositories';

export class SpotController {
  constructor(
    @repository(SpotRepository)
    public spotRepository : SpotRepository,
  ) {}

  @post('/spots', {
    responses: {
      '200': {
        description: 'Spot model instance',
        content: {'application/json': {schema: {'x-ts-type': Spot}}},
      },
    },
  })
  async create(@requestBody() spot: Spot): Promise<Spot> {
    return await this.spotRepository.create(spot);
  }

  @get('/spots/count', {
    responses: {
      '200': {
        description: 'Spot model count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async count(
    @param.query.object('where', getWhereSchemaFor(Spot)) where?: Where,
  ): Promise<Count> {
    return await this.spotRepository.count(where);
  }

  @get('/spots', {
    responses: {
      '200': {
        description: 'Array of Spot model instances',
        content: {
          'application/json': {
            schema: {type: 'array', items: {'x-ts-type': Spot}},
          },
        },
      },
    },
  })
  async find(
    @param.query.object('filter', getFilterSchemaFor(Spot)) filter?: Filter,
  ): Promise<Spot[]> {
    return await this.spotRepository.find(filter);
  }

  @patch('/spots', {
    responses: {
      '200': {
        description: 'Spot PATCH success count',
        content: {'application/json': {schema: CountSchema}},
      },
    },
  })
  async updateAll(
    @requestBody() spot: Spot,
    @param.query.object('where', getWhereSchemaFor(Spot)) where?: Where,
  ): Promise<Count> {
    return await this.spotRepository.updateAll(spot, where);
  }

  @get('/spots/{id}', {
    responses: {
      '200': {
        description: 'Spot model instance',
        content: {'application/json': {schema: {'x-ts-type': Spot}}},
      },
    },
  })
  async findById(@param.path.string('id') id: string): Promise<Spot> {
    return await this.spotRepository.findById(id);
  }

  @patch('/spots/{id}', {
    responses: {
      '204': {
        description: 'Spot PATCH success',
      },
    },
  })
  async updateById(
    @param.path.string('id') id: string,
    @requestBody() spot: Spot,
  ): Promise<void> {
    await this.spotRepository.updateById(id, spot);
  }

  @put('/spots/{id}', {
    responses: {
      '204': {
        description: 'Spot PUT success',
      },
    },
  })
  async replaceById(
    @param.path.string('id') id: string,
    @requestBody() spot: Spot,
  ): Promise<void> {
    await this.spotRepository.replaceById(id, spot);
  }

  @del('/spots/{id}', {
    responses: {
      '204': {
        description: 'Spot DELETE success',
      },
    },
  })
  async deleteById(@param.path.string('id') id: string): Promise<void> {
    await this.spotRepository.deleteById(id);
  }
}
