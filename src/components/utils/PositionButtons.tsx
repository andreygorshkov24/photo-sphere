import { HTMLProps, MouseEventHandler } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

type Props = HTMLProps<HTMLDivElement> & {
  onClickUp: MouseEventHandler<HTMLButtonElement>
  onClickDown: MouseEventHandler<HTMLButtonElement>
}

const PositionButtons = ({ onClickUp, onClickDown, ...other }: Props) => {
  return (
    <div {...other}>
      <button className="order-button" onClick={onClickUp ?? undefined}>
        <FontAwesomeIcon icon={['fas', 'arrow-up']} />
      </button>
      <button className="order-button" onClick={onClickDown ?? undefined}>
        <FontAwesomeIcon icon={['fas', 'arrow-down']} />
      </button>
    </div>
  );
};

export { PositionButtons };
