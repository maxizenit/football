package ru.borbotko.football.util.xmlhandler;

import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import ru.borbotko.football.entity.Amplua;

@Component
public class AmpluaXMLHandler extends XMLHandler<Amplua> {

  private static final String ENTITY_NAME = "amplua";
  private static final String CODE_ATTR = "code";
  private static final String DESCRIPTION_ATTR = "description";

  public AmpluaXMLHandler() {
    super(ENTITY_NAME);
  }

  @Override
  protected Amplua createEntityFromAttrs(NamedNodeMap attrs) {
    Amplua amplua = new Amplua();

    amplua.setCode(attrs.getNamedItem(CODE_ATTR).getNodeValue());
    amplua.setDescription(attrs.getNamedItem(DESCRIPTION_ATTR).getNodeValue());

    return amplua;
  }

  @Override
  protected void setAttrsFromEntity(Element element, Amplua entity) {
    element.setAttribute(CODE_ATTR, entity.getCode());
    element.setAttribute(DESCRIPTION_ATTR, entity.getDescription());
  }
}
