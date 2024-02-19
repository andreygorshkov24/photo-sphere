import { DragEvent, useEffect, useRef, useState } from 'react';
import { TProduct, TTheme } from '../model.ts';
import { addProductToThemeFolder, fetchProducts, fetchThemes, productDown, productUp } from '../server_api.ts';
import { Product } from './Product.tsx';
import { Position, Positionable } from './utils/Positionable.tsx';
import { ThemeFolder } from './ThemeFolder.tsx';

const Themes = () => {
  const [themes, setThemes] = useState<TTheme[]>([]);
  const [selectedTheme, setSelectedTheme] = useState<TTheme | null>(null);
  const [selectedThemeProducts, setSelectedThemeProducts] = useState<TProduct[]>([]);

  useEffect(() => {
    void (async () => {
      try {
        const themes = await fetchThemes();
        setThemes(themes);
      } catch (e: unknown) {
        console.error(e);
        setThemes([]);
      }
    })();
  }, []);

  const refreshSelectedTheme = async (theme: TTheme) => {
    setSelectedTheme(theme);
    await refreshSelectedThemeProducts(theme);
  };

  const refreshSelectedThemeProducts = async (theme: TTheme | null = selectedTheme) => {
    if (!theme) {
      setSelectedThemeProducts([]);
      return;
    }

    try {
      setSelectedThemeProducts(await fetchProducts(theme.id));
    } catch (e: unknown) {
      console.error(e);
      setSelectedThemeProducts([]);
    }
  };

  const targetThemeElement = useRef<HTMLDivElement | null>();
  const targetTheme = useRef<TTheme | null>();
  const targetProduct = useRef<TProduct | null>();

  const handleOnDragOver = (e: DragEvent<HTMLDivElement>, theme: TTheme) => {
    targetThemeElement.current = e.currentTarget;
    targetTheme.current = theme;
  };

  const handleOnDragStart = (product: TProduct) => targetProduct.current = product;
  const handleOnDragEnd = async (e: DragEvent<HTMLDivElement>) => {
    if (targetThemeElement.current) {
      const boundingClientRect = targetThemeElement.current.getBoundingClientRect();
      const isXValid = e.clientX > boundingClientRect.x && e.clientX < boundingClientRect.x + boundingClientRect.width;
      const isYValid = e.clientY > boundingClientRect.y && e.clientY < boundingClientRect.y + boundingClientRect.height;
      if (isXValid && isYValid && targetTheme.current && targetProduct.current) {
        try {
          await addProductToThemeFolder(targetProduct.current.id, targetTheme.current.id);
        } catch (e: unknown) {
          console.error(e);
        } finally {
          targetThemeElement.current = null;
          targetTheme.current = null;
          targetProduct.current = null;
        }
        await refreshSelectedThemeProducts(selectedTheme);
      }
    }
  };

  const handleOnClickUp = async (product: TProduct) => {
    try {
      await productUp(product.id);
      await refreshSelectedThemeProducts();
    } catch (e: unknown) {
      console.error(e);
    }
  };

  const handleOnClickDown = async (product: TProduct) => {
    try {
      await productDown(product.id);
      await refreshSelectedThemeProducts();
    } catch (e: unknown) {
      console.error(e);
    }
  };

  return (
    <div className="row">
      <div className="col-6 p-0">
        <Positionable position={Position.End}>
          <h3>Themes</h3>
        </Positionable>
        {!!themes && themes.map((theme, key) => {
          return (
            <div key={key} onClick={() => refreshSelectedTheme(theme)} onDragOver={(e) => handleOnDragOver(e, theme)}>
              <ThemeFolder {...theme} position={Position.Start} onClickUp={null} onClickDown={null} />
            </div>
          );
        })}
      </div>
      <div className="col-6 p-0">
        {!!selectedTheme ? (
          <>
            <Positionable position={Position.End}>
              <h3>Name: {selectedTheme.name}</h3>
              <span className="text-secondary">Description: {selectedTheme.description}</span>
              <hr />
              <span className="text-secondary">Products count: {selectedThemeProducts.length}</span>
            </Positionable>
            {selectedThemeProducts.map((product, key) => {
              return (
                <div key={key} onDragStart={() => handleOnDragStart(product)} onDragEnd={(e) => handleOnDragEnd(e)} draggable>
                  <Product {...product} position={Position.End} onClickUp={() => handleOnClickUp(product)} onClickDown={() => handleOnClickDown(product)} />
                </div>
              );
            })}
          </>
        ) : (
          <Positionable position={Position.End}>
            <h3>No selected theme...</h3>
          </Positionable>
        )}
      </div>
    </div>
  );
};

export { Themes };
