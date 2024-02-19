import { PropsWithChildren } from 'react';

enum Position {
  Start = 'Start',
  End = 'End'
}

type Props = PropsWithChildren & {
  position: Position
}

const Positionable = ({ children, position }: Props) => {
  return (
    <div className={position === Position.Start ? 'position-start' : 'position-end'}>
      {children}
    </div>
  );
};

export { Position, Positionable };
