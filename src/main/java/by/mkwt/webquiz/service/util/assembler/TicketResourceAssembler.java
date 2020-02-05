package by.mkwt.webquiz.service.util.assembler;

import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class TicketResourceAssembler {}
//implements ResourceAssembler<Ticket, Resource<Ticket>> {
//
//    @Override
//    public Resource<Ticket> toResource(Ticket ticket) {
//
//        return new Resource<>(ticket,
//                linkTo(methodOn(TicketController.class).getTicket(ticket.getId())).withSelfRel(),
//                linkTo(methodOn(TicketController.class).checkTicket(ticket.getId())).withRel("game"));
//    }
//
//}