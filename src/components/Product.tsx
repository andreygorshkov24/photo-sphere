import { TProduct } from '../model.ts';
import { Position, Positionable } from './utils/Positionable.tsx';
import { PositionButtons } from './utils/PositionButtons.tsx';

type Props = TProduct & {
  position: Position,
  onClickUp: () => {} | null,
  onClickDown: () => {} | null,
}

const Product = ({ name, description, position, onClickUp, onClickDown }: Props) => {
  return (
    <Positionable position={position}>
      <div className="card product">
        <div className="card-body">
          <div className="row">
            <div className="col-9 text-start">
              <h3>{name}</h3>
              <p>{description}</p>
            </div>
            {!!onClickUp && !!onClickDown &&
              <PositionButtons className="col-3 text-end" onClickUp={onClickUp} onClickDown={onClickDown} />}
          </div>

        </div>
      </div>
    </Positionable>
  );
};

export { Product };
