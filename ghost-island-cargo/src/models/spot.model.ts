import { Entity, model, property } from '@loopback/repository';

@model({
  name: 'client',
  settings: {
    strict: false,
    mongodb: {
      collection: 'spot',
    },
  },
})
export class Spot extends Entity {
  @property({
    type: 'string',
    id: true,
    required: true,
  })
  id: string;

  @property({
    type: 'string',
  })
  description?: string;

  @property({
    type: 'object',
    required: true,
  })
  location: object;


  constructor(data?: Partial<Spot>) {
    super(data);
  }
}
