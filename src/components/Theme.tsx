import { TTheme } from '../model.ts';
import { Position, Positionable } from './utils/Positionable.tsx';
import { PositionButtons } from './utils/PositionButtons.tsx';

type Props = TTheme & {
  position: Position,
  onClickUp: () => {} | null,
  onClickDown: () => {} | null,
}

const Theme = ({ name, description, position, onClickUp, onClickDown }: Props) => {
  return (
    <Positionable position={position}>
      <div className="card theme">
        <div className="card-body">
          <div className="row">
            <div className="col-9 text-start">
              <h3 className="ellipsis">Theme: {name}</h3>
              <p className="ellipsis">{description}</p>
            </div>
            {!!onClickUp && !!onClickDown &&
              <PositionButtons className="col-3 text-end" onClickUp={onClickUp} onClickDown={onClickDown} />}
          </div>
        </div>
      </div>
    </Positionable>
  );
};

export { Theme };
