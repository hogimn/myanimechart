import { Card } from "antd";

const CommonCard = ({
  title,
  extra,
  children,
  hoverable = true,
  cover,
  ...restProps
}) => {
  return (
    <Card
      title={title}
      extra={extra}
      hoverable={hoverable}
      cover={cover}
      {...restProps}
    >
      {children}
    </Card>
  );
};

CommonCard.Meta = Card.Meta;

export default CommonCard;
