import { TThemeFolder } from '../model.ts';
import { Position, Positionable } from './utils/Positionable.tsx';
import { PositionButtons } from './utils/PositionButtons.tsx';

type Props = TThemeFolder & {
  position: Position
  onClickUp: (() => {}) | null,
  onClickDown: (() => {}) | null,
}

const ThemeFolder = ({ name, description, position, onClickUp, onClickDown }: Props) => {
  return (
    <Positionable position={position}>
      <div className="card pack">
        <div className="card-body">
          <div className="row">
            <div className="col-9 text-start">
              <h3>Theme folder: {name}</h3>
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

export { ThemeFolder };
